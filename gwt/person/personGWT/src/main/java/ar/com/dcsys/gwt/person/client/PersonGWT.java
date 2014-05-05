package ar.com.dcsys.gwt.person.client;


import ar.com.dcsys.gwt.person.client.activity.PersonActivityMapper;
import ar.com.dcsys.gwt.person.client.activity.manage.ManagePersonsActivity;
import ar.com.dcsys.gwt.person.client.gin.Injector;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.place.PersonPlaceHistoryMapper;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;
import ar.com.dcsys.gwt.person.client.ui.AcceptsOneWidgetAdapter;
import ar.com.dcsys.gwt.person.client.ui.menu.Menu;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.SocketStateEventHandler;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

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
	  
	  SimplePanel sp = new SimplePanel();
	  RootPanel.get("content").add(sp);
	  AcceptsOneWidgetAdapter adapter = new AcceptsOneWidgetAdapter(sp);
	  
	  EventBus eventBus = injector.eventbus();
	  
	  // inicializo la parte de los activities y places
	  PlaceController pc = new PlaceController(eventBus);
	  PersonActivityMapper pam = injector.personActivityMapper();
	  ActivityManager am = new ActivityManager(pam,eventBus);
	  am.setDisplay(adapter);
	  
	  
	  // inicioalizo la parte de los history
	  PersonPlaceHistoryMapper historyMapper = GWT.create(PersonPlaceHistoryMapper.class);
	  final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
	  historyHandler.register(pc,eventBus,new UpdatePersonDataPlace());
	   
	  
	  final ManagePersonsActivity activity = injector.factory().managePersonsActivity(new ManagePersonsPlace());
	  
	  eventBus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
		@Override
		public void onOpen() {
			historyHandler.handleCurrentHistory();
		}
		
		@Override
		public void onClose() {
		}
	  });
	  
	  RootPanel pMenu = RootPanel.get("menu");
	  (new Menu(pc)).attachMenu(pMenu);
	  
	  try {
			injector.ws().open();
	  } catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  
  }
}
