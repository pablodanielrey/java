package ar.com.dcsys.gwt.messages.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class MessageEvent extends GwtEvent<MessageEventHandler> {

	public static GwtEvent.Type<MessageEventHandler> TYPE = new GwtEvent.Type<MessageEventHandler>();
	
	private final String type;
	private final String msg;
	
	public MessageEvent(String type, String msg) {
		this.type = type;
		this.msg = msg;
	}
	
	@Override
	public GwtEvent.Type<MessageEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageEventHandler handler) {
		handler.onMessage(this);
	}

	public String getMessage() {
		return msg;
	}

	public String getType() {
		return type;
	}

	
}
