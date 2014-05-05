package ar.com.dcsys.gwt.person.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.assistance.PersonAssistanceDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.shared.PersonFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class UpdatePersonDataActivity extends AbstractActivity implements UpdatePersonDataView.Presenter {

	private final PersonDataActivity personDataActivity;
	private final PersonAssistanceDataActivity personAssistanceDataActivity;
	private final UpdatePersonDataView updatePersonDataView;
	
	@Inject
	public UpdatePersonDataActivity(PersonsManager personsManager, PersonFactory personFactory, 
									AuthManager authManager,
									UpdatePersonDataView updatePersonDataView,
									PersonDataView personDataView, 
									PersonAssistanceDataView personAssistanceDataView,
									@Assisted UpdatePersonDataPlace place) {
		
		this.updatePersonDataView = updatePersonDataView;
		
		personDataActivity = new PersonDataActivity(personsManager, personFactory, authManager, personDataView, place);
		personAssistanceDataActivity = new PersonAssistanceDataActivity(personsManager, authManager, personAssistanceDataView);
		
	}
	
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		
		personDataActivity.setSelectionModel(selection);
		personAssistanceDataActivity.setSelectionModel(selection);
		
	}
	
	
	@Override
	public void start(AcceptsOneWidget containerPanel, EventBus eventBus) {
		
		updatePersonDataView.setPresenter(this);
		
		AcceptsOneWidget panel = updatePersonDataView.getBasicPersonData();
		personDataActivity.start(panel, eventBus);
		
		panel = updatePersonDataView.getAssistancePersonData();
		personAssistanceDataActivity.start(panel, eventBus);
		
		containerPanel.setWidget(updatePersonDataView);
		
	}
	
	@Override
	public void onStop() {
		personDataActivity.onStop();
		personAssistanceDataActivity.onStop();
		super.onStop();
	}

	@Override
	public void persist() {
		personDataActivity.persist();
		personAssistanceDataActivity.persist();
	}

}
