package ar.com.dcsys.gwt.ws.shared;

import ar.com.dcsys.gwt.message.shared.Message;

import com.google.gwt.event.shared.GwtEvent;

public class SocketMessageEvent extends GwtEvent<SocketMessageEventHandler> {

	public static GwtEvent.Type<SocketMessageEventHandler> TYPE = new GwtEvent.Type<SocketMessageEventHandler>();
	
	private final Message msg;
	
	public SocketMessageEvent(Message msg) {
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

	public Message getMessage() {
		return msg;
	}

}
