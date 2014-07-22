package ar.com.dcsys.gwt.messages.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface MessageEventHandler extends EventHandler {

	public void onMessage(MessageEvent event);
	
}
