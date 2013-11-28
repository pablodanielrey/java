package ar.com.dcsys.gwt.ws.client;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.ws.shared.SocketException;

public interface WebSocket {

	public void open() throws SocketException;
	public void close() throws SocketException;
	public void send(Message msg, WebSocketReceiver rec);

	
}
