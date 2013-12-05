package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.calendar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface CalendarViewResources extends ClientBundle {
	
	public static final CalendarViewResources  INSTANCE = GWT.create(CalendarViewResources.class);

	@Source("CalendarAdmin.css")
	public CalendarViewCss style();

}
