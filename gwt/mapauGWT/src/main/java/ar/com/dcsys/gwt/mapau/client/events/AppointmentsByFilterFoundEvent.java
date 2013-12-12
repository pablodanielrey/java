package ar.com.dcsys.gwt.mapau.client.events;

import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;

public class AppointmentsByFilterFoundEvent extends GwtEvent<AppointmentsByFilterFoundEventHandler> {

	public static final GwtEvent.Type<AppointmentsByFilterFoundEventHandler> TYPE = new GwtEvent.Type<AppointmentsByFilterFoundEventHandler>();
	
	private final List<MapauAppointment> apps;
	
	public AppointmentsByFilterFoundEvent(List<MapauAppointment> apps) {
		this.apps = apps;
	}
	
	public List<MapauAppointment> getAppointments() {
		return apps;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppointmentsByFilterFoundEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AppointmentsByFilterFoundEventHandler handler) {
		handler.onAppointmentsFound(this);
	}

}
