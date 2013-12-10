package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.List;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;


public class DeleteAppointmentEvent extends GwtEvent<DeleteAppointmentEventHandler> {
	
	public static final GwtEvent.Type<DeleteAppointmentEventHandler> TYPE = new GwtEvent.Type<DeleteAppointmentEventHandler>();
	
	private final MapauAppointment appointment;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public DeleteAppointmentEvent(MapauAppointment a, Receiver<List<MapauAppointment>> rec) {
		this.appointment = a;
		this.receiver = rec;
	}
	
	public MapauAppointment getAppointment() {
		return appointment;
	}
	
	public Receiver<List<MapauAppointment>> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DeleteAppointmentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DeleteAppointmentEventHandler handler) {
		handler.onDeleteAppointment(this);
	}

}
