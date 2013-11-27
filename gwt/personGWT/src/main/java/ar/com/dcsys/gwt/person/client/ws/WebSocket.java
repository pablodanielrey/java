package ar.com.dcsys.gwt.person.client.ws;

import java.util.HashMap;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.person.client.utils.GUID;

import com.google.gwt.core.client.GWT;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class WebSocket {

	private final MessageFactory messageFactory;
	private final Websocket socket;
	private WebSocketState wsState;
	
	private final HashMap<String,WebSocketReceiver> receivers;
	
	
	private final WebsocketListener wsListener = new WebsocketListener() {
		@Override
		public void onOpen() {
			wsState = WebSocketState.OPEN;
		}
		
		@Override
		public void onMessage(String json) {
			Message msg = MessageEncoderDecoder.decode(messageFactory,json);
			String id = msg.getId();
			MessageType type = msg.getType();

			if (MessageType.RETURN.equals(type)) {
				
				WebSocketReceiver rec = receivers.get(id);
				rec.onSuccess(msg);
				
			} else if (MessageType.ERROR.equals(type)) {
				
				WebSocketReceiver rec = receivers.get(id);
				rec.onFailure(null);
				
			}
		}
		
		@Override
		public void onClose() {
			wsState = WebSocketState.CLOSED;
		}
	};
	
	
	
	
	public WebSocket(String url) {

		messageFactory = GWT.create(MessageFactory.class);
		
		receivers = new HashMap<String,WebSocketReceiver>();
		
		socket = new Websocket(url);
		socket.addListener(wsListener);
		wsState = WebSocketState.CLOSED;		
		
	}
	
	public void open() {
		if (wsState == WebSocketState.CLOSED) {
			socket.open();
		}
	}
	
	public void close() {
		if (wsState == WebSocketState.OPEN) {
			socket.close();
		}
	}
	
	public void send(Message msg, WebSocketReceiver rec) {
		if (wsState != WebSocketState.OPEN) {
			rec.onFailure(new SocketClosedException());
			return;
		}

		String id = GUID.get();

		receivers.put(id, rec);
		msg.setId(id);
		
		String json = MessageEncoderDecoder.encode(msg);		
		socket.send(json);
	}
	

}
