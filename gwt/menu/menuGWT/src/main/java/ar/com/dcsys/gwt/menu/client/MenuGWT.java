package ar.com.dcsys.gwt.menu.client;

import ar.com.dcsys.gwt.menu.client.activity.MenuActivity;
import ar.com.dcsys.gwt.menu.client.activity.MenuActivityMapper;
import ar.com.dcsys.gwt.menu.client.gin.Injector;
import ar.com.dcsys.gwt.menu.client.place.MenuPlace;
import ar.com.dcsys.gwt.menu.client.place.MenuPlaceHistoryMapper;
import ar.com.dcsys.gwt.menu.client.ui.AcceptsOneWidgetAdapter;
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
public class MenuGWT implements EntryPoint {

	private Injector injector = GWT.create(Injector.class);
	
	public void onModuleLoad() {
		
		SimplePanel sp = new SimplePanel();
		RootPanel.get("content").add(sp);
		AcceptsOneWidgetAdapter adapter = new AcceptsOneWidgetAdapter(sp);
		
		EventBus eventBus = injector.eventBus();
		
		//inicializo la parte de los activities y places
		PlaceController pc = new PlaceController(eventBus);
		MenuActivityMapper mam = injector.menuActivityMapper();
		ActivityManager am = new ActivityManager(mam,eventBus);
		am.setDisplay(adapter);
		
		//inicializo la parte de los history
		MenuPlaceHistoryMapper historyMapper = GWT.create(MenuPlaceHistoryMapper.class);
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(pc,eventBus,new MenuPlace());
		
		final MenuActivity activity = injector.factory().menuActivity(new MenuPlace());
		
		eventBus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
			@Override
			public void onOpen() {
				historyHandler.handleCurrentHistory();
			}
			@Override
			public void onClose() {				
			}			
		});
		
		try {
			injector.ws().open();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
  
}
