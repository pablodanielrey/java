package ar.com.dcsys.gwt.mapau.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class CancelFiltersEvent extends GwtEvent<CancelFiltersHandler>  {

	public static final Type<CancelFiltersHandler> TYPE = new Type<CancelFiltersHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CancelFiltersHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CancelFiltersHandler handler) {
		handler.onCancelFilters();
	}
	
	public CancelFiltersEvent() {

	}
	
}
