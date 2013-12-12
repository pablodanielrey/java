package ar.com.dcsys.gwt.mapau.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class RefreshAppointmentsEvent extends GwtEvent<RefreshAppointmentsEventHandler> {

	public static GwtEvent.Type<RefreshAppointmentsEventHandler> TYPE = new GwtEvent.Type<RefreshAppointmentsEventHandler>();
	
	private final boolean onlyClient;
	
	public RefreshAppointmentsEvent(boolean onlyClient) {
		this.onlyClient = onlyClient;
	}

	public RefreshAppointmentsEvent() {
		this.onlyClient = false;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RefreshAppointmentsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RefreshAppointmentsEventHandler handler) {
		handler.onRefreshAppointmnets(this);
	}

}
