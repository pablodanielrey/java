package ar.com.dcsys.gwt.mapau.client;

import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.MainCalendarActivity;
import ar.com.dcsys.gwt.mapau.client.gin.Injector;
import ar.com.dcsys.gwt.mapau.client.gin.MapauAssistedInjectionFactory;
import ar.com.dcsys.gwt.mapau.client.place.MainCalendarPlace;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MapauGWT implements EntryPoint {
 

//  private final Messages messages = GWT.create(Messages.class);

	
  private final Injector injector = GWT.create(Injector.class);
	
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

	  EventBus eventbus = injector.eventBus();
	  
	  MapauAssistedInjectionFactory f = injector.factory();
	  MainCalendarActivity activity = f.mainCalendarActivity(new MainCalendarPlace());
	  
	  AcceptsOneWidget a = new AcceptsOneWidget() {
		@Override
		public void setWidget(IsWidget w) {
			  RootPanel.get().add(w);
		}
	  };
	  
	  activity.start(a, eventbus);
	  
  }
}
