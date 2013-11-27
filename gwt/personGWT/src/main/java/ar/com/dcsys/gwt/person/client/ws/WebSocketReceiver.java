package ar.com.dcsys.gwt.person.client.ws;

import ar.com.dcsys.gwt.message.shared.Message;

public interface WebSocketReceiver {

	public void onSuccess(Message message);
	public void onFailure(Throwable t);
	
}
