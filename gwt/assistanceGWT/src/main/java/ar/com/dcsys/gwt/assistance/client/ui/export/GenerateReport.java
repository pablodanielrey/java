package ar.com.dcsys.gwt.assistance.client.ui.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.SingleSelectionModel;

public class GenerateReport extends Composite implements GenerateReportView {

	private static GenerateReportUiBinder uiBinder = GWT.create(GenerateReportUiBinder.class);

	interface GenerateReportUiBinder extends UiBinder<Widget, GenerateReport> {
	}
	
	@UiField(provided=true) ValueListBox<GroupType> groupTypes;
	@UiField(provided=true) ValueListBox<Group> group;
	@UiField(provided=true) DateBox start;
	@UiField(provided=true) DateBox end;	
	@UiField(provided=true) ValueListBox<PERIODFILTER> report;
	@UiField Anchor exportedData;
	
	private Presenter p;
	private SingleSelectionModel<Group> groupSelection;
	private SingleSelectionModel<GroupType> groupTypeSelection;
	private SingleSelectionModel<PERIODFILTER> reportSelectionModel;
	
	private final ValueChangeHandler<PERIODFILTER> reportChangeHandler = new ValueChangeHandler<PERIODFILTER>() {
		@Override
		public void onValueChange(ValueChangeEvent<PERIODFILTER> event) {
			if (reportSelectionModel == null) {
				return;
			}
			PERIODFILTER item = report.getValue();
			reportSelectionModel.setSelected(item, true);
		}
	};	
	
	
	public void createDate() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		start = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));			
		end = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));			
	}
	
	private void createGroupTypes() {		
		groupTypes = new ValueListBox<GroupType>(new Renderer<GroupType>() {
			private String getValue(GroupType gt) {
				if (gt == null) {
					return "Mostrar Todos";
				}
				return gt.getDescription();
			}
			
			@Override
			public String render(GroupType object) {
				return getValue(object);
			}
			
			@Override
			public void render(GroupType object, Appendable appendable) throws IOException {
				appendable.append(getValue(object));
			}
		});
		groupTypes.addValueChangeHandler(new ValueChangeHandler<GroupType>() {
			@Override
			public void onValueChange(ValueChangeEvent<GroupType> event) {
				if (groupTypeSelection != null) {
					GroupType gt = groupTypes.getValue();
					groupTypeSelection.setSelected(gt, true);
				}
			}
		});			
	}
	
	public void createGroup() {
		group = new ValueListBox<Group>(new Renderer<Group>() {
			private String getValue(Group group) {
				return group.getName();
			}
			@Override
			public String render(Group object) {
				if (object == null) {
					return "Todos";
				}
				return getValue(object);
			}
			@Override
			public void render(Group object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
		group.addValueChangeHandler(new ValueChangeHandler<Group>() {
			@Override
			public void onValueChange(ValueChangeEvent<Group> event) {
				Group g = group.getValue();
				if (g == null) {
					groupSelection.clear();
					return;
				}
				groupSelection.setSelected(g, true);
			}
		});
	}
	
	private void createReportType() {
		report = new ValueListBox<PERIODFILTER>(new Renderer<PERIODFILTER>() {
			@Override
			public String render(PERIODFILTER object) {
				if (object == null) {
					return "";
				}
				return "Perídos : " + object.getDescription();
				
			}
			@Override
			public void render(PERIODFILTER object, Appendable appendable)	throws IOException {
				if (object == null) {
					return;
				}
				appendable.append("Períodos : " + object.getDescription());
			}
			
		});
		report.addValueChangeHandler(reportChangeHandler);
	}
	
	public GenerateReport() {
		createGroup();
		createGroupTypes();
		createDate();
		createReportType();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		exportedData.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportedData.setText("");
				exportedData.setHref("");
			}
		});		
	}



	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}



	@Override
	public void clear() {
		clearGroup();
		report.setAcceptableValues(new ArrayList());
		start.setValue(new Date());
		end.setValue(new Date());
	}

	private void clearGroup() {
		group.setValue(null);
		group.setAcceptableValues(new ArrayList<Group>());		
	}

	@Override
	public void setGroup(List<Group> groups) {
		clearGroup();
		/*
		 * Falta implementarlo
		GroupsSort.sort(groups);
		 */
		if (groups.size() > 0) {
			Group g = groups.get(0);
			this.group.setValue(g);
			if (groupSelection != null) {
				groupSelection.setSelected(g,true);
			}
		}
//		groups.add(null);			// por ahora no agrego el Todos.
		this.group.setAcceptableValues(groups);			
	}

	

	@Override
	public void setGroupSelection(SingleSelectionModel<Group> selection) {
		groupSelection = selection;
	}


	@Override
	public void setGroupTypes(List<GroupType> types) {
		List<GroupType> valuesTypes = new ArrayList<GroupType>();
		valuesTypes.add(null);
		valuesTypes.addAll(types);

		groupTypes.setValue(null);
		groupTypes.setAcceptableValues(valuesTypes);		
	}
	
	@Override
	public void setGroupTypesSelection(SingleSelectionModel<GroupType> selection) {
		this.groupTypeSelection = selection;
	}

	@Override
	public void setReports(List<PERIODFILTER> reports) {
		report.setValue(reports.get(0));
		report.setAcceptableValues(reports);
	}


	@Override
	public Date getStart() {
		return start.getValue();
	}



	@Override
	public Date getEnd() {
		return end.getValue();
	}
	
	@Override
	public void setExporedData(String data) {
//		exportedData.setTarget("_blank");
//		exportedData.setText("reporte.csv");
//		exportedData.setHref(data);
		
		// http://stackoverflow.com/questions/283956/is-there-any-way-to-specify-a-suggested-filename-when-using-data-uri/6943481#6943481
		//exportedData.getElement().setPropertyString("download", "reporte.csv");
	}	
	
	
	@UiHandler("commit")
	public void onCommit(ClickEvent event) {
		p.generateReport();
	}

	@Override
	public void setReportSelectionModel(SingleSelectionModel<PERIODFILTER> selection) {
		reportSelectionModel = selection;
	}

}
