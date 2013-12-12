package ar.com.dcsys.gwt.auth.client;

import ar.com.dcsys.gwt.auth.client.activity.login.LoginActivity;
import ar.com.dcsys.gwt.auth.client.gin.Injector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AuthGWT implements EntryPoint {

	private Injector injector = GWT.create(Injector.class);
	
	public void onModuleLoad() {
		  
		EventBus eventBus = injector.eventBus();

		AcceptsOneWidget panel = new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				RootPanel.get().add(w);
			}
		};
		
		LoginActivity login = injector.loginActivity();
		login.start(panel, eventBus);
		
	}
  
}
