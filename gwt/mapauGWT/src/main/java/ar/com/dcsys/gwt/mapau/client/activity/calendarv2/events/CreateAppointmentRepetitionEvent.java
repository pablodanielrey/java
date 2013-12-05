package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;


public class CreateAppointmentRepetitionEvent extends GwtEvent<CreateAppointmentRepetitionEventHandler> {

	public static final GwtEvent.Type<CreateAppointmentRepetitionEventHandler> TYPE = new GwtEvent.Type<CreateAppointmentRepetitionEventHandler>();
	
	
	private final MapauAppointment mapp;
	private final Receiver<List<MapauAppointment>> receiver;
	
	public CreateAppointmentRepetitionEvent(MapauAppointment app, Receiver<List<MapauAppointment>> rec) {
		this.mapp = app;
		this.receiver = rec;
	}
	
	public MapauAppointment getAppointment() {
		return mapp;
	}
	
	public Receiver<List<MapauAppointment>> getReceiver() {
		return receiver;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CreateAppointmentRepetitionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateAppointmentRepetitionEventHandler handler) {
		handler.onCreateAppointmentRepetition(this);
	}

}
