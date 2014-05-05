package ar.com.dcsys.gwt.assistance.client.activity.justification;

import java.util.ArrayList;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDateBean;
import ar.com.dcsys.data.justification.GeneralJustificationDateProvider;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.ui.justification.general.GeneralsJustificationView;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.events.MailChangeModifiedEvent;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class GeneralsJustificationActivity extends AbstractActivity implements GeneralsJustificationView.Presenter{

	private final GeneralsJustificationView view;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsManager;
	private EventBus eventBus;
	
	private final SingleSelectionModel<Justification> selectionJustification;
	private MultiSelectionModel<GeneralJustificationDate> generalJustificationSelection;
	private final List<GeneralJustificationDate> generalJustificationDateList = new ArrayList<GeneralJustificationDate>();
	
	private HandlerRegistration hr = null;
	
	private final JustificationModifiedEventHandler handler = new JustificationModifiedEventHandler() {

		@Override
		public void onJustificationModifiedEvent(JustificationModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clearData();
			update();
		}
		
	};
	
	private final GeneralJustificationDateProvider generalJustificationDateProvider = new GeneralJustificationDateProvider() {

		@Override
		public GeneralJustificationDate getNew() {
			return assistanceFactory.generalJustificationDate().as();
		}
		
	};
	
	@Inject
	public GeneralsJustificationActivity(JustificationsManager justificationsManager, AssistanceFactory assistanceFactory, GeneralsJustificationView view) {
		this.justificationsManager = justificationsManager;
		this.view = view;
		this.assistanceFactory = assistanceFactory;
		
		selectionJustification = new SingleSelectionModel<Justification>();
		selectionJustification.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
			}
		});
		
		generalJustificationSelection = new MultiSelectionModel<GeneralJustificationDate>();
		generalJustificationSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		 this.eventBus = eventBus;
		 this.view.setPresenter(this);
		 this.view.clear();
		 
		 view.setGeneralJustificationSelection(generalJustificationSelection);
		 view.setTypesSelectionModel(selectionJustification);
		 view.setProvider(generalJustificationDateProvider);
		 panel.setWidget(view);
		 
		 update();
		 search(new Date(), new Date());
		 
		 hr = eventBus.addHandler(JustificationModifiedEvent.TYPE, handler);
	}
	
	private void update() {
		justificationsManager.getJustifications(new Receiver<List<Justification>>() {

			@Override
			public void onSuccess(List<Justification> justifications) {
				if (view == null || justifications == null || justifications.size() < 1) {
					return;
				}
				view.setTypes(justifications);
				selectionJustification.setSelected(justifications.get(0), true);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}

	@Override
	public void onStop() {
		if (hr != null) {
			hr.removeHandler();
		}
		view.clear();
		view.setPresenter(null);
	}
	
	@Override
	public void persist() {
		/*
		 * TODO: falta controlar operationMode
		 */
		
		List<GeneralJustificationDate> gjds = view.getDates();
		if (gjds == null || gjds.size() < 1) {
			return;
		}
		
		justificationsManager.persist(gjds, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				showMessage("Las justificaciones se han creado correctamente");
				view.clearData();
				search(view.getStart(),view.getEnd());
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());				
			}
			
		});
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}

	@Override
	public void search(Date start, Date end) {
		justificationsManager.findGeneralJustificationDateBy(start, end, new Receiver<List<GeneralJustificationDate>>() {

			@Override
			public void onSuccess(List<GeneralJustificationDate> justifications) {
				view.clearGeneralJustificationDateList();
				generalJustificationDateList.clear();
				if(view == null || justifications == null || justifications.size() < 1) {
					return;
				}
				generalJustificationDateList.addAll(justifications);
				view.setGeneralJustificationDateList(justifications);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
				view.clearGeneralJustificationDateList();
			}
			
		});
	}

	@Override
	public void delete() {
		/*
		 * TODO: falta implementar el operationMode
		 */
		
		final List<GeneralJustificationDate> justifications = new ArrayList<GeneralJustificationDate>(generalJustificationSelection.getSelectedSet());
		if (justifications == null || justifications.size() <= 0) {
			return;
		}
		
		justificationsManager.removeGeneralJustificationDate(justifications, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				showMessage("Se ha eliminado correctamente las justificaciones seleccionadas");
				generalJustificationSelection.clear();
				generalJustificationDateList.removeAll(justifications);
				view.setGeneralJustificationDateList(generalJustificationDateList);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}

}
