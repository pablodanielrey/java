package ar.com.dcsys.gwt.mapau.client.gin;

import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.MainCalendarActivity;
import ar.com.dcsys.gwt.mapau.client.place.MainCalendarPlace;

public interface MapauAssistedInjectionFactory {

	public MainCalendarActivity mainCalendarActivity(MainCalendarPlace place);
	
	
}
