package ar.com.dcsys.gwt.ws.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import ar.com.dcsys.gwt.message.shared.MessagesFactoryImpl;
import ar.com.dcsys.gwt.message.shared.Method;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;


@ServerEndpoint(value = "/websockets")
public class Websockets {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());

	private Map<String,Session> sessions = new HashMap<>();
	private MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);

	
	////////////////////////
	//// parche hasta que este CDI 
	///////////////////////
	
	private MessageEncoderDecoder getMessageEncoderDecoder() {
		MessageEncoderDecoder med = new MessageEncoderDecoder(messageFactory);
		return med;
	}

	private MessagesFactory getMessagesFactory() {
		MessagesFactory mf = new MessagesFactoryImpl(messageFactory);
		return mf;
	}
	
	
	//////////////////////////
	//////////////////////////
	
	

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
		MessageEncoderDecoder med = getMessageEncoderDecoder();
		Message msg = med.decode(json);

		if (msg.getType().equals(MessageType.FUNCTION)) {

			// nada
			
		}
		
		
		Message resp = messageFactory.message().as();
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

