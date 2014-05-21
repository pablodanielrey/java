package ar.com.dcsys.gwt.assistance.client;


import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.assistance.client.gin.Injector;
import ar.com.dcsys.gwt.assistance.client.place.AssistancePlaceHistoryMapper;
import ar.com.dcsys.gwt.assistance.client.place.DailyPeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.ui.common.AcceptsOneWidgetAdapter;
import ar.com.dcsys.gwt.assistance.client.ui.menu.Menu;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;

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
public class AssistanceGWT implements EntryPoint {


	private final Injector injector = GWT.create(Injector.class);
	
	public void onModuleLoad() {
		  SimplePanel sp = new SimplePanel();
		  RootPanel.get("content").add(sp);
		  AcceptsOneWidgetAdapter adapter = new AcceptsOneWidgetAdapter(sp);
		  
		  EventBus eventBus = injector.eventbus();
		  
		  // inicializo la parte de los activities y places
		  PlaceController pc = new PlaceController(eventBus);
		  AssistanceActivityMapper pam = injector.assistanceActivityMapper();
		  ActivityManager am = new ActivityManager(pam,eventBus);
		  am.setDisplay(adapter);
		  
		  
		  // inicioalizo la parte de los history
		  AssistancePlaceHistoryMapper historyMapper = GWT.create(AssistancePlaceHistoryMapper.class);
		  final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		  historyHandler.register(pc,eventBus,new DailyPeriodsPlace());
		   
//		  final PinAuthDataActivity activity = injector.factory().pinAuthDataActivity(new PinAuthDataPlace());
		  
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
