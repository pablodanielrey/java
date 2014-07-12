package ar.com.dcsys.gwt.auth.client;

import ar.com.dcsys.gwt.auth.client.activity.login.LoginActivity;
import ar.com.dcsys.gwt.auth.client.gin.Injector;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AuthGWT implements EntryPoint {

	private Injector injector = GWT.create(Injector.class);
	
	public void onModuleLoad() {

		WebSocket ws = injector.ws();
		final AuthManager authManager = injector.authManager();

		final EventBus eventBus = injector.eventBus();
		eventBus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
			@Override
			public void onOpen() {

				AcceptsOneWidget panel = new AcceptsOneWidget() {
					@Override
					public void setWidget(IsWidget w) {
						RootPanel.get().add(w);
					}
				};

				LoginActivity login = injector.loginActivity();
				login.start(panel, eventBus);
				
			}
			
			@Override
			public void onClose() {
			}
		});
		
		
		
		try {
			ws.open();
		} catch (SocketException e) {
			Window.alert(e.getMessage());
		}
	}
  
}
