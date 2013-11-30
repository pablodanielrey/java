package ar.com.dcsys.gwt.ws.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NamingException;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.utils.server.BeanManagerLocator;
import ar.com.dcsys.gwt.utils.server.BeanManagerUtils;


@ServerEndpoint(value = "/websockets")
public class Websockets {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());

	private Map<String,Session> sessions = new HashMap<>();

	private MessagesFactory messagesFactory = null;
	private MessageEncoderDecoder encoderDecoder = null;
	private MessageFactory messageFactory = null;
	
	private MessageFactory getMessageFactory() {
		if (messageFactory != null) {
			return messageFactory;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			messageFactory = BeanManagerUtils.lookup(MessageFactory.class,bm);
			return messageFactory;
		} catch (NamingException e) {
			return null;
		}
	}
	
	private MessagesFactory getMessagesFactory() {
		if (messagesFactory != null) {
			return messagesFactory;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			messagesFactory = BeanManagerUtils.lookup(MessagesFactory.class,bm);
			return messagesFactory;
		} catch (NamingException e) {
			return null;
		}
	}
	
	private MessageEncoderDecoder getEncoderDecoder() {
		if (encoderDecoder != null) {
			return encoderDecoder;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			encoderDecoder = BeanManagerUtils.lookup(MessageEncoderDecoder.class,bm);
			return encoderDecoder;
		} catch (NamingException e) {
			return null;
		}
	}	
	
	
	/*
	 * No se puede usar porque no funca CDI en websockets todavía.
	@Inject
	public Websockets(MessageFactory messageFactory, MessageEncoderDecoder messageEncoderDecoder, MessagesFactory messagesFactory) {
		logger.info("iniciando constructor inyectado");
		this.messageFactory = messageFactory;
		this.messageEncoderDecoder = messageEncoderDecoder;
		this.messagesFactory = messagesFactory;
	}
	*/
	

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.log(Level.INFO,"onOpen");

		Session s = sessions.get(session.getId());
		if (s == null) {
			sessions.put(session.getId(), session);
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("onClose - ").append(reason.getCloseCode().getCode()).append(" - ").append(reason.getReasonPhrase());
		logger.log(Level.INFO,sb.toString());
		
		sessions.remove(session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		logger.log(Level.WARNING,error.getMessage(),error);
	}

	
	@OnMessage
	public void onMessage(Session session, String json) {
		logger.log(Level.INFO,"Msg : " + json);
		
		
		// decodifico el mensaje:
		MessageEncoderDecoder med = getEncoderDecoder();
		Message msg = med.decode(json);

		
		Message resp = getMessageFactory().message().as();
		resp.setId(msg.getId());
		resp.setType(MessageType.RETURN);
		resp.setPayload("funco super");
		String response = med.encode(resp);
		
		
 		for (Session s1 : sessions.values()) {
			if (s1.isOpen()) {
				try {
					s1.getBasicRemote().sendText(response);
				} catch (IOException e) {
					logger.log(Level.WARNING,e.getMessage(),e);
				}
			}
		}
	}
	
	
}

