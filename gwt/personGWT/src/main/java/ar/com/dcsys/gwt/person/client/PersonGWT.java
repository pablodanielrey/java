package ar.com.dcsys.gwt.person.client;


import ar.com.dcsys.gwt.person.client.activity.manage.ManagePersonsActivity;
import ar.com.dcsys.gwt.person.client.gin.Injector;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.ui.AcceptsOneWidgetAdapter;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.SocketStateEventHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
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

	  RootPanel p = RootPanel.get();
	  final AcceptsOneWidgetAdapter adapter = new AcceptsOneWidgetAdapter(p);
	  
	  final ManagePersonsActivity activity = injector.factory().managePersonsActivity(new ManagePersonsPlace());
	  
	  final EventBus eventBus = injector.eventbus();
	  eventBus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
		@Override
		public void onOpen() {
			activity.start(adapter, eventBus);
		}
		
		@Override
		public void onClose() {
		}
	});
	  
	  
	  /*
	  UpdatePersonDataActivity upda = injector.factory().updatePersonDataActivity(new PersonDataPlace());
	  upda.setSelectionModel(new SingleSelectionModel<Person>());
	  upda.start(adapter, eventBus);
	  */
	  
	 
	  
	  try {
			injector.ws().open();
	  } catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  
	  
  }
}
