package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.event.shared.GwtEvent;



public class ThisOrRelatedEvent extends GwtEvent<ThisOrRelatedEventHandler> {

	public static final GwtEvent.Type<ThisOrRelatedEventHandler> TYPE = new GwtEvent.Type<ThisOrRelatedEventHandler>();
	private final Receiver<Boolean> receiver;
	
	public ThisOrRelatedEvent(Receiver<Boolean> receiver) {
		this.receiver = receiver;
	}

	public Receiver<Boolean> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ThisOrRelatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ThisOrRelatedEventHandler handler) {
		handler.onThisOrRelated(this);
	}
	

	
	
}
