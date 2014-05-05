package ar.com.dcsys.gwt.ws.shared;

import ar.com.dcsys.gwt.message.shared.Event;
import ar.com.dcsys.gwt.message.shared.Message;

import com.google.gwt.event.shared.EventHandler;

public interface SocketMessageEventHandler extends EventHandler {

	public void onMessage(Message msg);
	
}
