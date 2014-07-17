package ar.com.dcsys.gwt.assistance.client.activity.person;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.PersonDataManager;
import ar.com.dcsys.gwt.assistance.client.ui.person.PersonDataView;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.ui.manage.ManagePersonsView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.SingleSelectionModel;

public class ManagePersonsActivity extends AbstractActivity implements ManagePersonsView.Presenter, PersonDataView.Presenter {

	private static final Logger logger = Logger.getLogger(ManagePersonsActivity.class.getName());
	
	private final ManagePersonsView view;
	private final PersonDataView personDataView;
	
	private final PersonsManager personsManager;
	private final PersonDataManager personDataManager;
	
	private final SingleSelectionModel<Person> selection = new SingleSelectionModel<Person>();
	
	@Inject
	public ManagePersonsActivity(ManagePersonsView view, PersonDataView personDataView, PersonsManager personsManager, PersonDataManager personDataManager) {
		this.view = view;
		this.personDataView = personDataView;
		this.personsManager = personsManager;
		this.personDataManager = personDataManager;
	}
	
	@Override
	public void updateUsers() {
		personsManager.findAll(new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> t) {
				if (view == null) {
					return;
				}
				view.setPersons(t);
			}
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
		});
		
	}
	
	@Override
	public void enroll() {
		Person person = selection.getSelectedObject();
		if (person == null) {
			logger.log(Level.SEVERE,"selection.person == null");
			return;
		}
		personDataManager.enroll(person.getId(), new Receiver<Boolean>() {
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
			@Override
			public void onSuccess(Boolean t) {
				logger.log(Level.INFO, "usuario enrolado exitósamente");
			}
		});
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.clear();
		view.setSelectionModel(selection);
		
		personDataView.setPresenter(this);
		
		
		FlowPanel principalView = new FlowPanel();
		principalView.add(view);
		principalView.add(personDataView);
		
		panel.setWidget(principalView);
		
	}
	
	@Override
	public void onStop() {
		view.clear();
		view.setPresenter(null);
	}

}
