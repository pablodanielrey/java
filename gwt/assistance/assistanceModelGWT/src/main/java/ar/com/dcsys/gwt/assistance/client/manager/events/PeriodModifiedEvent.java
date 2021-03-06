package ar.com.dcsys.gwt.assistance.client.manager.events;

import com.google.gwt.event.shared.GwtEvent;

public class PeriodModifiedEvent extends GwtEvent<PeriodModifiedEventHandler> {

	public static final com.google.gwt.event.shared.GwtEvent.Type<PeriodModifiedEventHandler> TYPE = new com.google.gwt.event.shared.GwtEvent.Type<PeriodModifiedEventHandler>(); 
	
	private final String id;
	
	public PeriodModifiedEvent(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	@Override
	protected void dispatch(PeriodModifiedEventHandler handler) {
		handler.onPeriodModifiedEvent(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PeriodModifiedEventHandler> getAssociatedType() {
		return TYPE;
	}

	
}
