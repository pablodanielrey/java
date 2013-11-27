package ar.com.dcsys.gwt.ws.client;

import ar.com.dcsys.gwt.message.shared.Message;

public interface WebSocketReceiver {

	public void onSuccess(Message message);
	public void onFailure(Throwable t);
	
}
