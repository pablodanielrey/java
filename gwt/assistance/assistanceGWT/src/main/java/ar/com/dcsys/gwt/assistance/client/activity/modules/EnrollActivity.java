package ar.com.dcsys.gwt.assistance.client.activity.modules;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.PersonDataManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.modules.PersonModule;

import com.google.gwt.view.client.SingleSelectionModel;

public class EnrollActivity implements PersonModule.Activity, EnrollView.Presenter {

	private static final Logger logger = Logger.getLogger(EnrollActivity.class.getName());
	
	private SingleSelectionModel<Person> selection;
	private final PersonDataManager personDataManager;
	private final EnrollView view;
	
	public EnrollView getView() {
		return view;
	}
	
	
	@Inject
	public EnrollActivity(PersonDataManager personDataManager, EnrollView view) {
		this.personDataManager = personDataManager;
		this.view = view;
		view.setPresenter(this);
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void accept() {
		// no se hace nada cuando se aceptan datos de la persona.
	}

	
	
	@Override
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		this.selection = selection;
	}

	@Override
	public void enroll() {
		if (selection == null) {
			logger.log(Level.SEVERE,"selection == null");
			return;
		}
		
		Person person = selection.getSelectedObject();
		if (person == null) {
			logger.log(Level.SEVERE,"person == null");
			return;
		}
		
		String id = person.getId();
		if (id == null) {
			logger.log(Level.SEVERE,"person.id == null");
			return;
		}
		
		personDataManager.enroll(id, new Receiver<String>() {
			@Override
			public void onSuccess(String t) {
				if (t != null) {
					logger.log(Level.INFO,"Enrolado exitosamente");
				} else {
					logger.log(Level.INFO,"falla en el enrolado");
				}
			}
			
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
		});
	}
	
	
	
}
