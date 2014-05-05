package ar.com.dcsys.gwt.mapau.client.events;

import java.util.List;

import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.event.shared.GwtEvent;

public class FindAppointmentsByFiltersEvent extends GwtEvent<FindAppointmentsByFiltersHandler>  {

	public static final Type<FindAppointmentsByFiltersHandler> TYPE = new Type<FindAppointmentsByFiltersHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FindAppointmentsByFiltersHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FindAppointmentsByFiltersHandler handler) {
		handler.onFindAppointmentsByFilters(this);
	}
	
	
	private final List<FilterValue<?>> filters;
	
	
	public FindAppointmentsByFiltersEvent(List<FilterValue<?>> filters) {
		this.filters = filters;
	}

	public List<FilterValue<?>> getFilters() {
		return filters;
	}

	
}
