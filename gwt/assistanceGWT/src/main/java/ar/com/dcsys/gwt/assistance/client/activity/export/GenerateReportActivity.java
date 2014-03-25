package ar.com.dcsys.gwt.assistance.client.activity.export;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.assistance.client.ui.export.GenerateReportView;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class GenerateReportActivity extends AbstractActivity implements GenerateReportView.Presenter{

	private AssistanceRequestFactory rf;
	private PersonRequestFactory prf;
	private GenerateReportView view;
	
	private EventBus eventBus;
	
	private final SingleSelectionModel<Group> groupSelection;
	private final SingleSelectionModel<GroupType> groupTypeSelection;
	private final SingleSelectionModel<PERIODFILTER> reportSelectionModel;
	private final List<PERIODFILTER> periodFilters = Arrays.asList(PERIODFILTER.values());
	
	
	@Inject
	public GenerateReportActivity(PersonRequestFactory prf, AssistanceRequestFactory rf, GenerateReportView view, @Assisted GenerateReportPlace place){
		this.rf = rf;
		this.prf = prf;
		this.view = view;
		
		groupSelection = new SingleSelectionModel<Group>();
		groupSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
			}
			
		});
		groupTypeSelection = new SingleSelectionModel<GroupType>();
		groupTypeSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				GroupType type = groupTypeSelection.getSelectedObject();
				findGroupsByType(type);
			}
		});

		reportSelectionModel = new SingleSelectionModel<PERIODFILTER>();
	}
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		
		view.setPresenter(this);
		view.clear();
		view.setGroupSelection(groupSelection);
		view.setGroupTypesSelection(groupTypeSelection);
		view.setReportSelectionModel(reportSelectionModel);
		
		panel.setWidget(view);
		
		update();
	}

	
	@Override
	public void onStop() {
		this.eventBus = null;
		view.clear();
		
		groupSelection.clear();
		groupTypeSelection.clear();
	}

	private void update () {
		updateGroupTypes();
		updateReports();
	}
	
	
	private void showError(String message) {
		eventBus.fireEvent(new MessageDialogEvent());
	}
	
	
	private void updateGroupTypes() {
		prf.groupRequest().findAllTypes().fire(new Receiver<List<GroupType>>() {
			@Override
			public void onSuccess(List<GroupType> types) {
				if (types == null) {
					return;
				}
				groupTypeSelection.clear();
				view.setGroupTypes(types);
			}
			@Override
			public void onFailure(ServerFailure error) {
				showError(error.getMessage());
			}
		});
	}
	
	
	private void findGroupsByType(GroupType type) {
		
		Receiver<List<Group>> receiver = new Receiver<List<Group>>() {
			@Override
			public void onSuccess(List<Group> groups) {
				if (groups == null) {
					return;
				}
				groupSelection.clear();
				view.setGroup(groups);
			}
			@Override
			public void onFailure(ServerFailure error) {
				showError(error.getMessage());
			}
		};
		
		if (type == null) {
			prf.groupRequest().findAll().fire(receiver);
		} else {
			prf.groupRequest().findByType(type).fire(receiver);
		}
	}

	
	private void updateReports() {
		view.setReports(periodFilters);
		reportSelectionModel.setSelected(periodFilters.get(0), true);		
	}
	
	@Override
	public void generateReport() {
		Group group = groupSelection.getSelectedObject();
		Date start = view.getStart();
		Date end = view.getEnd();
		if (group == null) {
			return;
		}
		exportPeriods(group, start, end);
	}

	private String encodeXFormUrlEncoded(Map<String,String> params) {
		StringBuilder data = new StringBuilder();
		for (String k : params.keySet()) {
			String value = URL.encodeQueryString(params.get(k));
			if (data.length() > 0) {
				data.append("&");
			}
			data.append(k).append("=").append(value);
		}
		return data.toString();
	}	
	
	/*
	 * Genera la exportacion de los períodos pero usando dataUri y RequestBuilder
	 */
	/*
	private void exportPeriods(Group group, Date start, Date end) {
				
		String groupId = group.getId();
		
		DateTimeFormat defaultF = DateTimeFormat.getFormat("dd_MM_yyyy");
		String strStart = defaultF.format(start);
		String strEnd = defaultF.format(end);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("group",groupId);
		params.put("start",strStart);
		params.put("end", strEnd);
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,GWT.getHostPageBaseURL() + "PersonReportServlet");
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		String encodedData = encodeXFormUrlEncoded(params);
		rb.setRequestData(encodedData);
		rb.setCallback(new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String data = response.getText();
				String dataUri = "data:text/csv;charset=utf-8;base64," + data;
				Window.open(dataUri, "_blank", "");
				view.setExporedData(dataUri);
			}
			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert("Error : " + exception.getMessage());
			}
		});
		
		try {
			rb.send();
		} catch (RequestException e) {
			Window.alert("Error enviando request : " + e.getMessage());
		}	
	}
	*/

	/**
	 * Genera la exportacion de los periodos usando un FormPanel
	 * @param group
	 * @param start
	 * @param end
	 */
	private void exportPeriods(Group group, Date start, Date end) {
		
		String groupId = group.getId();
		
		DateTimeFormat defaultF = DateTimeFormat.getFormat("dd_MM_yyyy");
		String strStart = defaultF.format(start);
		String strEnd = defaultF.format(end);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("group",groupId);
		params.put("start",strStart);
		params.put("end", strEnd);
		
		// llamo al servlet.
		
		String target = String.valueOf(Random.nextInt());
		
		FormPanel fp = new FormPanel();
		fp.setAction(GWT.getHostPageBaseURL() + "PersonReportServlet");
		fp.setMethod("POST");
		fp.getElement().setAttribute("target", target);

		FlowPanel data = new FlowPanel();
		fp.add(data);
		
		TextBox periodFilter = new TextBox();
		periodFilter.setName("periodFilter");
		PERIODFILTER pf = reportSelectionModel.getSelectedObject();
		periodFilter.setText(pf.toString());
		data.add(periodFilter);		
		
		TextBox text = new TextBox();
		text.setName("group");
		text.setValue(groupId);
		data.add(text);

		text = new TextBox();
		text.setName("start");
		text.setValue(strStart);
		data.add(text);
		
		text = new TextBox();
		text.setName("end");
		text.setValue(strEnd);
		data.add(text);
		
		RootPanel.get().add(fp);
		
		Window.open("", target, "");
		fp.submit();
		
		RootPanel.get().remove(fp);		
	}
}
