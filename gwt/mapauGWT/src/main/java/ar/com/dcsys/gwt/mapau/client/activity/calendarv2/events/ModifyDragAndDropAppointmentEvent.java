package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ModifyDragAndDropAppointmentEvent extends GwtEvent<ModifyDragAndDropAppointmentEventHandler> {

	public static final GwtEvent.Type<ModifyDragAndDropAppointmentEventHandler> TYPE = new GwtEvent.Type<ModifyDragAndDropAppointmentEventHandler>();
	
	private final Date start;
	private final Date end;
	private final MapauAppointment appointment;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public ModifyDragAndDropAppointmentEvent(Date start, Date end, MapauAppointment appointment, Receiver<List<MapauAppointment>> rec) {
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
	public com.google.gwt.event.shared.GwtEvent.Type<ModifyDragAndDropAppointmentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModifyDragAndDropAppointmentEventHandler handler) {
		handler.onModifyDragAndDropEvent(this);
	}

}
