package ar.com.dcsys.gwt.assistance.client.activity.periods;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.manager.events.PeriodModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.PeriodModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.ui.period.person.PeriodsAssignationPersonView;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;

import com.google.inject.Inject;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class PeriodsAssignationPersonActivity extends AbstractActivity implements PeriodsAssignationPersonView.Presenter {

	private final PeriodsAssignationPersonView view;
	private final AssistanceFactory assistanceFactory;
	private final PeriodsManager periodsManager;
	private final PersonsManager personsManager;
	
	private final SingleSelectionModel<Person> personSelection;
	private Person personSelected;
	
	private final SingleSelectionModel<PeriodType> selectionType;
	
	private EventBus eventBus;
	
	private HandlerRegistration hr = null;
	
	private final PeriodModifiedEventHandler handler = new PeriodModifiedEventHandler() {
		@Override
		public void onPeriodModifiedEvent(PeriodModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clearPeriodsData();
			if (personSelected == null ) {
				return;
			}
			getPeriods(personSelected);
		}
	};
	
	@Inject
	public PeriodsAssignationPersonActivity(PersonsManager personsManager, PeriodsManager periodsManager, AssistanceFactory assistanceFactory, PeriodsAssignationPersonView view) {
		this.periodsManager = periodsManager;
		this.assistanceFactory = assistanceFactory;
		this.personsManager = personsManager;
		this.view = view;
		
		personSelection = new SingleSelectionModel<Person>();
		personSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				PeriodsAssignationPersonActivity.this.view.clearPeriodsData();
				Person person = personSelection.getSelectedObject();
				if (person == null) {
					PeriodsAssignationPersonActivity.this.view.setEnabledNewPeriod(false);
					personSelected = null;
					return;
				}

				PeriodsAssignationPersonActivity.this.view.setEnabledNewPeriod(true);
				personSelected = person;
				getPeriods(person);
			}			
		});
		
		selectionType = new SingleSelectionModel<PeriodType>();
		selectionType.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		view.clear();
		
		view.setSelectionModel(personSelection);
		view.setTypesSelectionModel(selectionType);
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
		super.onStop();
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	private void update() {
		updatePersons(false);
		updateTypes();
	}
	
	private void getPeriods(Person person) {
		this.periodsManager.findPeriodsAssignationsBy(person, new Receiver<List<PeriodAssignation>>() {

			@Override
			public void onSuccess(List<PeriodAssignation> periodsAssignation) {
				view.clearPeriodsData();
				if (periodsAssignation == null || periodsAssignation.size() <= 0) {
					return;
				}
				view.setPeriodsData(periodsAssignation);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}
	
	private void updateTypes() {
		this.periodsManager.findAllTypesPeriods(new Receiver<List<PeriodType>>() {

			@Override
			public void onSuccess(List<PeriodType> types) {
				if (types == null) {
					return;
				}
				view.setTypes(types);
				selectionType.setSelected(types.get(0), true);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}

	@Override
	public void updatePersons(boolean isAll) {
		personSelected = null;
		personSelection.clear();
		if (isAll) {
			findAllPerson();
		} else {
			findPersonsWithPeriodAssignation();
		}
	}	
	
	private void findAllPerson() {
		personsManager.findAll(new Receiver<List<Person>>() {

			@Override
			public void onSuccess(List<Person> response) {
				if (response == null || response.size() <= 0) {
					return;
				}
				view.setPersons(response);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}
	
	private void findPersonsWithPeriodAssignation() {
		periodsManager.findPersonsWithPeriodAssignation(new Receiver<List<Person>>(){

			@Override
			public void onSuccess(List<Person> response) {
				if (response == null || response.size() <= 0) {
					return;
				}
				view.setPersons(response);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}

	@Override
	public void remove(PeriodAssignation periodAssignation) {
		if (periodAssignation == null || personSelected == null) {
			return;
		}
		this.periodsManager.remove(personSelected, periodAssignation, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				PeriodsAssignationPersonActivity.this.getPeriods(personSelected);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}

	@Override
	public void create() {
		if (personSelected == null) {
			return;
		}
		
		Date date = view.getDate();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		PeriodType type = selectionType.getSelectedObject();
		
		PeriodAssignation periodAssignation = assistanceFactory.periodAssignation().as();
		periodAssignation.setStart(date);
		periodAssignation.setType(type);
		
		periodsManager.persist(personSelected, periodAssignation, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				PeriodsAssignationPersonActivity.this.getPeriods(personSelected);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
		});
	}

}
