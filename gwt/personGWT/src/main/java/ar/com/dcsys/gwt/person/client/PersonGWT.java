package ar.com.dcsys.gwt.person.client;

import ar.com.dcsys.gwt.person.PersonProxy;
import ar.com.dcsys.gwt.person.client.gin.Injector;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView.Presenter;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PersonGWT implements EntryPoint {
 

//  private final Messages messages = GWT.create(Messages.class);

	
	private final Injector injector = GWT.create(Injector.class);
	
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

	  
	  UpdatePersonDataView updv = injector.updatePersonDataView();
	  final PersonDataView view = injector.personDataView();
	  
	  updv.getBasicPersonData().setWidget(view);
	  RootPanel.get().add(updv);

	  
	  Presenter p = new UpdatePersonDataView.Presenter() {
		  
		  private PersonsManager personsManager = injector.personsManager(); 
		  
		@Override
		public void persist() {
			Window.alert("persistiendo persona");
			
			PersonProxy person = getPerson();
			personsManager.persist(person);
		}
		
		private PersonProxy getPerson() {
			
			String name = view.getName();
			String lastName = view.getLastName();
			String dni = view.getDni();
			
			PersonProxy person = personsManager.getPerson();
			person.setName(name);
			person.setLastName(lastName);
			person.setDni(dni);
			
			return person;
		}
		
	  };
	  
	  
	  updv.setPresenter(p);
	  
	  
  }
}
