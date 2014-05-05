package ar.com.dcsys.gwt.ws.shared.event;

import com.google.gwt.event.shared.EventHandler;

public interface SocketStateEventHandler extends EventHandler {

	public void onOpen();
	public void onClose();
	
}
