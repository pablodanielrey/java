package ar.com.dcsys.gwt.ws.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.utils.client.GUID;
import ar.com.dcsys.gwt.ws.shared.InvalidUrlException;
import ar.com.dcsys.gwt.ws.shared.SocketClosedException;
import ar.com.dcsys.gwt.ws.shared.SocketException;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.SocketStateEvent;

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
	
	private final MessageEncoderDecoder messageEncoderDecoder;
	private final EventBus eventBus;
	private final HashMap<String,WebSocketReceiver> receivers;
	
	
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
		public void onMessage(String json) {
			Message msg = messageEncoderDecoder.decode(Message.class, json);
			
			if (MessageType.RETURN.equals(msg.getType())) {
			
				String id = msg.getId();
				if (id == null) {
					logger.log(Level.SEVERE,"MensajeType == RETURN and msg.id == null");
					return;
				}
					
				WebSocketReceiver rec = receivers.get(id);
				if (rec == null) {
					logger.log(Level.SEVERE,"MessageTYpe == RETURN and msg.id != null and receiver == null");
					return;
				}

				try {
					rec.onSuccess(msg);
				} catch(Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				return;
			}

			/**
			 * En caso de error y no tener id el mensaje se dispara un SocketMessageEvent, si tiene id se llama al onFailure del id correspondiente 
			 * con el payload como msg del SocketException nuevo que se crea.
			 */
			if (MessageType.ERROR.equals(msg.getType())) {
				
				String id = msg.getId();
				if (id == null) {
					logger.log(Level.FINE,"MensajeType == ERROR and msg.id == null -- fire SocketMessageEvent");
					try {
						eventBus.fireEvent(new SocketMessageEvent(msg));
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
					return;
				}
					
				WebSocketReceiver rec = receivers.get(id);
				if (rec == null) {
					logger.log(Level.SEVERE,"MessageTYpe == ERROR and msg.id != null and receiver == null");
					return;
				}

				try {
					rec.onFailure(new SocketException(msg.getPayload()));
				} catch(Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				return;
			}
			
			
			if (MessageType.EVENT.equals(msg.getType())) {
				try {
					eventBus.fireEvent(new SocketMessageEvent(msg));
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				return;
			}
			
			logger.log(Level.SEVERE,"MessageType unknown");
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
	public WebSocketImpl(EventBus eventBus, MessageEncoderDecoder messageEncoderDecoder) {
		this.url = getUrl();
		this.eventBus = eventBus;
		this.messageEncoderDecoder = messageEncoderDecoder;
		receivers = new HashMap<String,WebSocketReceiver>();
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
	public void send(Message msg, WebSocketReceiver rec) {
		if (wsState != WebSocketState.OPEN) {
			rec.onFailure(new SocketClosedException());
			return;
		}

		if (msg == null) {
			rec.onFailure(new MessageException("msg == null"));
		}
		
		String id = GUID.get();
		msg.setId(id);
		receivers.put(id, rec);
		
		String json = messageEncoderDecoder.encode(Message.class, msg);
		try {
			socket.send(json);

		} catch (Exception e) {
			receivers.remove(id);
			rec.onFailure(new MessageException(e));
		}
	}
	

}
