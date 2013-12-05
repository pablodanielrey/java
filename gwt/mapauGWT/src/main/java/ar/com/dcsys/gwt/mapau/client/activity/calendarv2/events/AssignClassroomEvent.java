package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class AssignClassroomEvent extends GwtEvent<AssignClassroomEventHandler> {

	public static GwtEvent.Type<AssignClassroomEventHandler> TYPE = new GwtEvent.Type<AssignClassroomEventHandler>();
	
	
	private final MapauAppointment appointment;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public AssignClassroomEvent(MapauAppointment app, Receiver<List<MapauAppointment>> rec) {
		this.appointment = app;
		this.receiver = rec;
	}
	
	public MapauAppointment getAppointment() {
		return appointment;
	}
	
	public Receiver<List<MapauAppointment>> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AssignClassroomEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AssignClassroomEventHandler handler) {
		handler.onAssignClassroomEvent(this);
	}

	
	
}
