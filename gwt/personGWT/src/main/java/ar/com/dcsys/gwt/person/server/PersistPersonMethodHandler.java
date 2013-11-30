package ar.com.dcsys.gwt.person.server;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.message.server.MessageHandlers;
import ar.com.dcsys.gwt.message.server.MethodHandler;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonProxy;

@Singleton
public class PersistPersonMethodHandler implements MethodHandler {

	private static final Logger logger = Logger.getLogger(PersistPersonMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessagesFactory mf;
	
	@Inject
	public PersistPersonMethodHandler(PersonEncoderDecoder encoderDecoder, MessagesFactory messagesFactory) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
	}

	/**
	 * Se registra como handler cuando es llamado por el evento disparado por CDI
	 * @param mh
	 */
	public void register(@Observes MessageHandlers mh) {
		mh.addHandler(this);
	}
	
	@Override
	public boolean handles(Method method) {
		return PersonMethods.persist.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		
		logger.info("se llamo a " + PersonMethods.persist);
		
		String params = method.getParams();
		PersonProxy person = encoderDecoder.decode(params);
		
		logger.info("Nombre " + person.getName());
		logger.info("Appelido : " + person.getLastName());
		logger.info("DNI : " + person.getDni());
		
		sendResponse(msg, transport);
		
	}
	
	
	private void sendResponse(Message r, MessageTransport transport) {
		String id = UUID.randomUUID().toString();
		
		Message msg = mf.response(r);
		msg.setPayload(id);
		
		try {
			transport.send(msg);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
}
