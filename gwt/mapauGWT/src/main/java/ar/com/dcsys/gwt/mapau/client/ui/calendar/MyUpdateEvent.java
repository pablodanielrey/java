package ar.com.dcsys.gwt.mapau.client.ui.calendar;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.gwt.event.shared.GwtEvent;

public class MyUpdateEvent extends GwtEvent<MyUpdateEventHandler> {

	public static final GwtEvent.Type<MyUpdateEventHandler> TYPE = new GwtEvent.Type<MyUpdateEventHandler>();
	
	private final Appointment original;
	private final Appointment cloned;
	
	public MyUpdateEvent(Appointment original, Appointment cloned) {
		this.original = original;
		this.cloned = cloned;
	}
	
	public Appointment getOriginal() {
		return original;
	}

	public Appointment getCloned() {
		return cloned;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MyUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MyUpdateEventHandler handler) {
		handler.onMyUpdate(this);
	}

}
