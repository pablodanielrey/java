package ar.com.dcsys.gwt.person.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEvent;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEventHandler;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class LoggedPersonActivity extends AbstractActivity {

	private final UpdatePersonDataActivity updatePersonDataActivity;
	private final SingleSelectionModel<Person> selection = new SingleSelectionModel<Person>();
	private final PersonsManager personsManager;
	private EventBus eventBus;
	
	@Inject
	public LoggedPersonActivity(AssistedInjectionFactory aif, 
								PersonsManager personsManager, 
								AuthManager authManager,
								@Assisted UpdatePersonDataPlace place) {
		
		this.personsManager = personsManager;
		this.updatePersonDataActivity = aif.updatePersonDataActivity(place);
	}
	
	
	private HandlerRegistration hr;
	private PersonModifiedEventHandler handler = new PersonModifiedEventHandler() {
		@Override
		public void onPersonModified(PersonModifiedEvent event) {
			String personId = event.getId();
			final Person person = selection.getSelectedObject();
			if (person == null || personId == null || person.getId() == null || (!personId.equals(person.getId()))) {
				return;
			}
			
			// si la persona que cambio es la misma que esta seleccionada, etnocnes la pido nuevamnete.
			selection.clear();
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					selectLoggedPerson();
				}
			});
		}
	};
	
	@Override
	public void start(AcceptsOneWidget containerPanel, EventBus eventBus) {
		this.eventBus = eventBus;

		selection.clear();
		
		updatePersonDataActivity.setSelectionModel(selection);
		updatePersonDataActivity.start(containerPanel, eventBus);
		
		selectLoggedPerson();
		
		hr = eventBus.addHandler(PersonModifiedEvent.TYPE, handler);
	}
	
	@Override
	public void onStop() {
		hr.removeHandler();
		selection.clear();
		updatePersonDataActivity.onStop();
		super.onStop();
	}

	private void showMessage(String msg) {
		eventBus.fireEvent(new MessageDialogEvent(msg));
	}
	
	
	private void selectLoggedPerson() {
		personsManager.getLoggedPerson(new Receiver<Person>() {
			@Override
			public void onSuccess(Person person) {
				selection.clear();
				if (person != null) {
					selection.setSelected(person, true);
				}
			}
			@Override
			public void onError(String t) {
				showMessage(t);
			}
		});
	}
	
}

