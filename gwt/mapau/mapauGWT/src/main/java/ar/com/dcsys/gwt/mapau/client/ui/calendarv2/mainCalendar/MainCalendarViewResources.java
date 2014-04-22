package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.mainCalendar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface MainCalendarViewResources extends ClientBundle {
	
	public static final MainCalendarViewResources  INSTANCE = GWT.create(MainCalendarViewResources.class);

	@Source("MainCalendarAdmin.css")
	public MainCalendarViewCss style();

}
