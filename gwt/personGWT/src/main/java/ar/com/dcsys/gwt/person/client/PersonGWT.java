package ar.com.dcsys.gwt.person.client;

import java.util.List;

import ar.com.dcsys.gwt.person.client.gin.Injector;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.Receiver;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView.Presenter;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.shared.PersonProxy;
import ar.com.dcsys.gwt.ws.shared.SocketException;

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
		public void findall() {
			  personsManager.findAll(new Receiver<List<PersonProxy>>() {
					@Override
					public void onSuccess(List<PersonProxy> t) {
						
						Window.alert(String.valueOf(t.size()));
						
						PersonProxy pp = t.get(2);
						
						view.setName(pp.getName());
						view.setLastName(pp.getLastName());
						view.setDni(pp.getDni());
						
					}
					@Override
					public void onFailure(Throwable t) {
						if (t == null) {
							Window.alert("findall error = null");
						} else {
							Window.alert("error en findAll : " + t.getMessage());
						}
					}
				  });					
		}
		  
		@Override
		public void persist() {
			PersonProxy person = getPerson();
			personsManager.persist(person, new Receiver<String>() {
				@Override
				public void onSuccess(String t) {
					Window.alert(t);
				}
				@Override
				public void onFailure(Throwable t) {
					if (t == null) {
						Window.alert("Error persistiendo");						
					} else {
						Window.alert(t.getMessage());
					}
					
				}
			});
		}
		
		private PersonProxy getPerson() {

			String name = view.getName();
			String lastName = view.getLastName();
			String dni = view.getDni();
			
			PersonProxy person = injector.personFactory().person().as();
			person.setName(name);
			person.setLastName(lastName);
			person.setDni(dni);
			
			return person;
		}
		
	  };
	  
	  
	  updv.setPresenter(p);


	  try {
		injector.ws().open();
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
}
