package ar.com.dcsys.gwt.ws.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.messages.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.gwt.utils.client.GUID;
import ar.com.dcsys.gwt.ws.shared.InvalidUrlException;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.event.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class WebSocketImpl implements WebSocket {
	
	private static final Logger logger = Logger.getLogger(WebSocket.class.getName());
	
	private String url;
	private Websocket socket;
	private WebSocketState wsState;
	
	private final EventBus eventBus;
	private final HashMap<String,TransportReceiver> receivers;
	
	
	private final WebsocketListener wsListener = new WebsocketListener() {
		@Override
		public void onOpen() {
			wsState = WebSocketState.OPEN;
			try {
				eventBus.fireEvent(new SocketStateEvent(true));
			} catch(Exception e) {
				logger.log(Level.SEVERE,e.getMessage(),e);
			}
		}
		
		@Override
		public void onClose() {
			wsState = WebSocketState.CLOSED;
			try {
				eventBus.fireEvent(new SocketStateEvent(false));
			} catch(Exception e) {
				logger.log(Level.SEVERE,e.getMessage(),e);
			}
		}
		
		@Override
		public void onMessage(String msg) {

			String[] msgd = MessageEncoderDecoder.decode(msg);
			String id = msgd[0];
			msg = msgd[1];
			
			TransportReceiver rec = receivers.get(id);
			if (rec != null) {
				try {
					rec.onSuccess(msg);
				} catch(Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
			
			eventBus.fireEvent(new SocketMessageEvent(msg));
		}
	};
	


	
	/**
	 * REtorna la url donde esta configurado el websocket del lado del servidor.
	 * @return
	 */
	private final String getUrl() {
		String host = GWT.getHostPageBaseURL();
		int from = 0;
		if (host.toLowerCase().startsWith("http://")) {
			from = 5;
		} else if (host.toLowerCase().startsWith("https://")) {
			from = 6;
		}
		String url = "ws://" + host.substring(from) + "websockets";		
		return url;
	}
	
	
	@Inject
	public WebSocketImpl(EventBus eventBus) {
		this.url = getUrl();
		this.eventBus = eventBus;
		receivers = new HashMap<String,TransportReceiver>();
		wsState = WebSocketState.CLOSED;		
	}
	
	@Override
	public void open() throws SocketException {
		
		if (url == null) {
			throw new InvalidUrlException();
		}
		
		try {
			
			if (socket == null) {
				socket = new Websocket(url);
				socket.addListener(wsListener);
				wsState = WebSocketState.CLOSED;
			}
		
			if (wsState == WebSocketState.CLOSED) {
				socket.open();
			}
			
		} catch (Exception e) {
			throw new SocketException(e);
		}

	}
	
	@Override
	public void close() throws SocketException {
		if (socket == null) {
			throw new SocketException();
		}
		
		try {
			if (wsState == WebSocketState.OPEN) {
				socket.close();
			}
		} catch (Exception e) {
			throw new SocketException(e);
		}
	}
	
	@Override
	public void send(String msg, TransportReceiver rec) {
		String id = GUID.get().replace("-", "");
		send(id,msg,rec);
	}
	
	@Override
	public void send(String id, String msg, TransportReceiver rec) {
		if (wsState != WebSocketState.OPEN) {
			rec.onFailure("La conexión con el servidor se encuentra cerrada");
		}

		if (msg == null) {
			rec.onFailure("msg == null");
		}
		
		try {		
			String emsg = MessageEncoderDecoder.encode(id,msg);
			receivers.put(id, rec);
			socket.send(emsg);
			
		} catch (Exception e) {
			receivers.remove(id);
			rec.onFailure(e.getMessage());
		}
	}
	

}
