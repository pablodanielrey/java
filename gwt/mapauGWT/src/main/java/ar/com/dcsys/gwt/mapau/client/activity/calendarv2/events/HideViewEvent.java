package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import com.google.gwt.event.shared.GwtEvent;

public class HideViewEvent extends GwtEvent<HideViewEventHandler> {

	public static final GwtEvent.Type<HideViewEventHandler> TYPE = new GwtEvent.Type<HideViewEventHandler>();
	private final String view;
	
	public HideViewEvent(String view) {
		this.view = view;
	}
	
	public String getView() {
		return view;
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HideViewEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HideViewEventHandler handler) {
		handler.hideView(this);
	}

}
