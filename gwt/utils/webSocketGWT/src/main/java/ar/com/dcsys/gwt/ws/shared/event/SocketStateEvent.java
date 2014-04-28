package ar.com.dcsys.gwt.ws.shared.event;

import com.google.gwt.event.shared.GwtEvent;

public class SocketStateEvent extends GwtEvent<SocketStateEventHandler> {

	public static final GwtEvent.Type<SocketStateEventHandler> TYPE = new GwtEvent.Type<SocketStateEventHandler>();
	
	private final boolean open;
	
	public SocketStateEvent(boolean open) {
		this.open = open;
	}
	
	@Override
	public GwtEvent.Type<SocketStateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SocketStateEventHandler handler) {
		if (open) {
			handler.onOpen();
		} else {
			handler.onClose();
		}
	}

}
