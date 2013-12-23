package ar.com.dcsys.gwt.person.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
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
	
	@Override
	public void start(AcceptsOneWidget containerPanel, EventBus eventBus) {
		this.eventBus = eventBus;

		selection.clear();
		
		updatePersonDataActivity.setSelectionModel(selection);
		updatePersonDataActivity.start(containerPanel, eventBus);
		
		selectLoggedPerson();
	}
	
	@Override
	public void onStop() {
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
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
		});
	}
	
}

