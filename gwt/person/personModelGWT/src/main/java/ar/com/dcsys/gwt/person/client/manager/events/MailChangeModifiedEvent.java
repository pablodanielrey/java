package ar.com.dcsys.gwt.person.client.manager.events;

import com.google.gwt.event.shared.GwtEvent;

public class MailChangeModifiedEvent extends GwtEvent<MailChangeModifiedEventHandler> {

	public static final com.google.gwt.event.shared.GwtEvent.Type<MailChangeModifiedEventHandler> TYPE = new com.google.gwt.event.shared.GwtEvent.Type<MailChangeModifiedEventHandler>();
	
	@Override
	protected void dispatch(MailChangeModifiedEventHandler handler) {
		handler.onMailChangeModifiedEvent(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MailChangeModifiedEventHandler> getAssociatedType() {
		return TYPE;
	}
	
	
	
}
