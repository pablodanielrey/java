package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;

import com.google.gwt.event.shared.GwtEvent;

public class CalendarTypeEvent extends GwtEvent<CalendarTypeEventHandler> {

	public static final GwtEvent.Type<CalendarTypeEventHandler> TYPE = new GwtEvent.Type<CalendarTypeEventHandler>();
	
	private final CalendarType calendarType;
	
	public CalendarTypeEvent(CalendarType type) {
		this.calendarType = type; 
	}
	
	public CalendarType getCalendarType() {
		return calendarType;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CalendarTypeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CalendarTypeEventHandler handler) {
		handler.onCalendarType(this);
	}

	
}
