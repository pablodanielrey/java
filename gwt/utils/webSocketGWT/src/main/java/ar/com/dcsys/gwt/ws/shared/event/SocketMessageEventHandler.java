package ar.com.dcsys.gwt.ws.shared.event;

import com.google.gwt.event.shared.EventHandler;

public interface SocketMessageEventHandler extends EventHandler {

	public void onMessage(String msg);
	
}
