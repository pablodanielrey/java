package ar.com.dcsys.gwt.assistance.client.activity.justification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.manager.events.PeriodModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.PeriodModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEvent;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public class JustificationPersonActivity extends AbstractActivity implements JustificationPersonView.Presenter {

	private final JustificationPersonView view;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsManager;
	private final PeriodsManager periodsManager;
	private EventBus eventBus = null;
	
	private final MultiSelectionModel<Person> selection;
	private final MultiSelectionModel<JustificationDate> selectionJustificationDate;
	private final SingleSelectionModel<Justification> selectionJustification;
	
	private final List<Justification> justifications = new ArrayList<Justification>();
	private final List<JustificationDate> justificationDateList = new ArrayList<JustificationDate>();
	
	private HandlerRegistration hr = null;
	
	private final PeriodModifiedEventHandler handler = new PeriodModifiedEventHandler() {

		@Override
		public void onPeriodModifiedEvent(PeriodModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clear();
			update();
		}
		
	};
	
	
	@Inject
	public JustificationPersonActivity(JustificationsManager justificationsManager, PeriodsManager periodsManager, AssistanceFactory assistanceFactory, JustificationPersonView view) {
		this.view = view;
		this.assistanceFactory = assistanceFactory;
		this.justificationsManager = justificationsManager;
		this.periodsManager = periodsManager;
		
		selection = new MultiSelectionModel<Person>();
		selection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<Person> persons = selection.getSelectedSet();
				JustificationPersonActivity.this.view.clearPersonData();
				JustificationPersonActivity.this.view.clearStatistic();
				selectionJustificationDate.clear();
				if (persons == null) {
					JustificationPersonActivity.this.view.setEnabledJustifications(false);
				} else {
					JustificationPersonActivity.this.view.setEnabledJustifications(true);
					if (justifications != null && justifications.size() > 0) {
						selectionJustification.setSelected(justifications.get(0), true);
					}
				}
			}
		});
		
		selectionJustification = new SingleSelectionModel<Justification>();
		selectionJustification.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {				
			}
		});
		
		selectionJustificationDate = new MultiSelectionModel<JustificationDate>();
		selectionJustificationDate.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<JustificationDate> justificationDates = selectionJustificationDate.getSelectedSet();
				
				if (justificationDates == null || justificationDates.size() <= 0) {
					//deshabilito el boton eliminar
					JustificationPersonActivity.this.view.setEnabledRemoveButton(false);
				} else {
					//habilito el boton eliminar
					JustificationPersonActivity.this.view.setEnabledRemoveButton(true);
				}
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		view.clear();
		view.getViewSelectionJustificationDate().setPresenter(this);
		view.setSelectionModel(selection);
		view.setJustificationDateSelectionModel(selectionJustificationDate);
		view.setTypesSelectionModel(selectionJustification);
		
		panel.setWidget(view);
		update();
		hr = eventBus.addHandler(PeriodModifiedEvent.TYPE, handler);
		
	}
	
	@Override
	public void onStop() {
		if (hr != null) {
			hr.removeHandler();
		}
		view.clear();
		view.setPresenter(null);
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	private void update() {
		this.periodsManager.findPersonsWithPeriodAssignation(new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> persons) {
				if (view == null || persons == null) {
					return;
				}
				view.setPersons(persons);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
		});
	}

	@Override
	public void persist() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearPersonsSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearJustificationSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findBy(Date start, Date end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeJustificationDates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateStatistics() {
		// TODO Auto-generated method stub
		
	}

	
	

}
