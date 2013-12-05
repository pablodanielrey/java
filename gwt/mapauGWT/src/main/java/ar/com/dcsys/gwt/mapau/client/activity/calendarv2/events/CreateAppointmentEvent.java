package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class CreateAppointmentEvent extends GwtEvent<CreateAppointmentEventHandler> {

	public static final GwtEvent.Type<CreateAppointmentEventHandler> TYPE = new GwtEvent.Type<CreateAppointmentEventHandler>();
	
	private final Date date;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public CreateAppointmentEvent(Date date, Receiver<List<MapauAppointment>> rec) {
		this.date = date;
		this.receiver = rec;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Receiver<List<MapauAppointment>> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CreateAppointmentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateAppointmentEventHandler handler) {
		handler.onCreateAppointment(this);
	}
	

}
