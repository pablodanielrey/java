package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.List;
import java.util.Map;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView.Operation;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class OpenAppointmentEvent extends GwtEvent<OpenAppointmentEventHandler> {

	public static final GwtEvent.Type<OpenAppointmentEventHandler> TYPE = new GwtEvent.Type<OpenAppointmentEventHandler>();
	
	private final MapauAppointment mapp;
	private final Map<Operation,Receiver<List<MapauAppointment>>> handlers;
	
	public OpenAppointmentEvent(MapauAppointment mapp, Map<Operation,Receiver<List<MapauAppointment>>> handlers) {
		this.handlers = handlers;
		this.mapp = mapp;
	}
	
	public Map<Operation, Receiver<List<MapauAppointment>>> getHandlers() {
		return handlers;
	}
	
	public MapauAppointment getMapp() {
		return mapp;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OpenAppointmentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenAppointmentEventHandler handler) {
		handler.onOpenAppointment(this);
	}
	
}
