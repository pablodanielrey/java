package ar.com.dcsys.gwt.assistance.client.manager.events;

import com.google.gwt.event.shared.GwtEvent;

public class JustificationModifiedEvent extends GwtEvent<JustificationModifiedEventHandler> {
	
	public static final com.google.gwt.event.shared.GwtEvent.Type<JustificationModifiedEventHandler> TYPE = new com.google.gwt.event.shared.GwtEvent.Type<JustificationModifiedEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<JustificationModifiedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(JustificationModifiedEventHandler handler) {
		handler.onJustificationModifiedEvent(this);
	}

}
