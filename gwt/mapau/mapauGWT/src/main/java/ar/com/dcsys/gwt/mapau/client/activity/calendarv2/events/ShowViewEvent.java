package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import com.google.gwt.event.shared.GwtEvent;

public class ShowViewEvent extends GwtEvent<ShowViewEventHandler> {

	public static final GwtEvent.Type<ShowViewEventHandler> TYPE = new GwtEvent.Type<ShowViewEventHandler>();
	private final String view;
	
	public ShowViewEvent(String view) {
		this.view = view;
	}
	
	public String getView() {
		return view;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ShowViewEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowViewEventHandler handler) {
		handler.showView(this);
	}

}
