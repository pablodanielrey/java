package ar.com.dcsys.gwt.ws.server;

import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.Session;

import ar.com.dcsys.gwt.messages.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.messages.shared.TransportEvent;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;

@Singleton
public class TransportEventSender {

	private static final Logger logger = Logger.getLogger(TransportEventSender.class.getName());
	private final SessionRegister sessions;
	
	@Inject
	public TransportEventSender(SessionRegister sessions) {
		this.sessions = sessions;
	}
	
	
	/*
	 * Envío de eventos desde el server hacia el cliente.
	 */
	private void onTransportEvent(@Observes TransportEvent event) {
		String type = event.getType();
		String msg = event.getMessage();
		TransportReceiver tr = event.getTransportReceiver();
		sendEvent(type + ";" + msg,tr);
	}	
	
	/**
	 * Envía un evento a todas las sesiones abiertas.
	 * @param msg
	 * @param rec
	 */
	private void sendEvent(String msg, TransportReceiver rec) {
		try {
			String emsg = MessageEncoderDecoder.encode(MessageEncoderDecoder.EVENT, MessageEncoderDecoder.BROADCAST, msg);
			for (String sid : sessions.getIds()) {
				Session s = sessions.get(sid);
				if (!s.isOpen()) {
					sessions.unregisterSession(s);
				} else {
					s.getBasicRemote().sendText(emsg);
				} 
			}
			rec.onSuccess(null);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
			rec.onFailure(e.getMessage());
		}
	}		
	
	
}
