package ar.com.dcsys.gwt.ws.shared.event;

import com.google.gwt.event.shared.GwtEvent;

public class SocketMessageEvent extends GwtEvent<SocketMessageEventHandler> {

	public static GwtEvent.Type<SocketMessageEventHandler> TYPE = new GwtEvent.Type<SocketMessageEventHandler>();
	
	private final String msg;
	
	public SocketMessageEvent(String msg) {
		this.msg = msg;
	}
	
	@Override
	public GwtEvent.Type<SocketMessageEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SocketMessageEventHandler handler) {
		handler.onMessage(getMessage());
	}

	public String getMessage() {
		return msg;
	}

}
