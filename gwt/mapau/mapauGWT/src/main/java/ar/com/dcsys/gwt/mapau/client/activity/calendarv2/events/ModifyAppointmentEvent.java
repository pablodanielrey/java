package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;


public class ModifyAppointmentEvent extends GwtEvent<ModifyAppointmentEventHandler> {

	public static final GwtEvent.Type<ModifyAppointmentEventHandler> TYPE = new GwtEvent.Type<ModifyAppointmentEventHandler>();
	
	private final Date start;
	private final Date end;
	private final MapauAppointment appointment;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public ModifyAppointmentEvent(Date start, Date end, MapauAppointment appointment, Receiver<List<MapauAppointment>> rec) {
		this.start = start;
		this.end = end;
		this.appointment = appointment;
		this.receiver = rec;
	}
	
	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public MapauAppointment getAppointment() {
		return appointment;
	}
	
	public Receiver<List<MapauAppointment>> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ModifyAppointmentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyAppointmentEventHandler handler) {
		handler.onModifyAppointment(this);
	}

}
