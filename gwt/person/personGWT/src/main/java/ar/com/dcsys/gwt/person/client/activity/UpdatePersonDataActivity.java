package ar.com.dcsys.gwt.person.client.activity;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.modules.PersonModule;
import ar.com.dcsys.gwt.person.client.modules.PersonPortal;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class UpdatePersonDataActivity extends AbstractActivity implements UpdatePersonDataView.Presenter {

	private final PersonDataActivity personDataActivity;
	private final UpdatePersonDataView updatePersonDataView;
	
	@Inject
	public UpdatePersonDataActivity(PersonsManager personsManager,
									MailChangesManager mailManager,
									AuthManager authManager,
									UpdatePersonDataView updatePersonDataView,
									PersonDataView personDataView, 
									@Assisted UpdatePersonDataPlace place) {
		
		this.updatePersonDataView = updatePersonDataView;
		
		personDataActivity = new PersonDataActivity(personsManager, mailManager, authManager, personDataView, place);
		
	}
	
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		
		personDataActivity.setSelectionModel(selection);
	
		for (PersonModule m : PersonPortal.getPortal().getModules()) {
			m.getActivity().setSelectionModel(selection);
		}
		
	}
	
	
	/**
	 * da start a los módulos de person.
	 */
	private void startModules() {
		for (PersonModule m : PersonPortal.getPortal().getModules()) {
			m.getActivity().start();
		}
	}
	
	/**
	 * stop a los modulos de person.
	 */
	private void stopModules() {
		for (PersonModule m : PersonPortal.getPortal().getModules()) {
			m.getActivity().stop();
		}
	}
		
	
	@Override
	public void start(AcceptsOneWidget containerPanel, EventBus eventBus) {
	
		updatePersonDataView.setPresenter(this);
		
		AcceptsOneWidget panel = updatePersonDataView.getBasicPersonData();
		personDataActivity.start(panel, eventBus);
		
		containerPanel.setWidget(updatePersonDataView);

		startModules();
	}
	
	@Override
	public void onStop() {
		personDataActivity.onStop();

		stopModules();
		
		super.onStop();
	}

	@Override
	public void persist() {
		personDataActivity.persist();

		for (PersonModule m : PersonPortal.getPortal().getModules()) {
			m.getActivity().accept();
		}
		
	}

}
