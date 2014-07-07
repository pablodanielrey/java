package ar.com.dcsys.gwt.assistance.client.activity.auth;


import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.PersonDataManager;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PinAuthDataActivity extends AbstractActivity implements PinAuthDataView.Presenter {

	private static final Logger logger = Logger.getLogger(PinAuthDataActivity.class.getName());
	private final PinAuthDataView view;
	private final PersonDataManager personDataManager;
	private final PersonsManager personsManager;
	
	
	@Inject
	public PinAuthDataActivity(PinAuthDataView view, @Assisted PinAuthDataPlace place, PersonDataManager personDataManager, PersonsManager personsManager) {
		this.view = view;
		this.personDataManager = personDataManager;
		this.personsManager = personsManager;
	} 
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.clear();
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
		super.onStop();
	}
	
	@Override
	public void persist() {
		
		if (view == null) {
			logger.log(Level.SEVERE, "view == null");
			return;
		}
		
		
		final String pin = view.getPin();
		
		if (pin == null) {
			logger.log(Level.SEVERE,"pin == null");
			return;
		}
		
		personsManager.getLoggedPerson(new Receiver<Person>() {
			@Override
			public void onSuccess(Person t) {
				String id = t.getId();
				personDataManager.setPin(id, pin, new Receiver<Boolean>() {
					@Override
					public void onError(String error) {
						logger.log(Level.SEVERE, error);
					}
					@Override
					public void onSuccess(Boolean t) {
						logger.log(Level.INFO,"pin modificado correctamente");
					}
				});
				
			}
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
		});
		
	}
	
}
