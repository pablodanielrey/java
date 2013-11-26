package ar.com.dcsys.gwt.person.client;

import ar.com.dcsys.gwt.person.client.gin.Injector;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
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

	  PersonDataView view = injector.personDataView();
	  RootPanel.get().add(view);
	  
  }
}
