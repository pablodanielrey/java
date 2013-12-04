package ar.com.dcsys.gwt.person.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.message.server.MessageHandlers;
import ar.com.dcsys.gwt.message.server.MethodHandler;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.PersonsManager;

@Singleton
public class PersistPersonMethodHandler implements MethodHandler {

	private static final Logger logger = Logger.getLogger(PersistPersonMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final PersonsManager personsModel;
	
	@Inject
	public PersistPersonMethodHandler(PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory,
									  PersonsManager personsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.personsModel = personsModel;
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
		
		String params = method.getParams();
		Person person = encoderDecoder.decode(Person.class,params);
		
		try {
			String id = personsModel.persist(person);
			sendResponse(msg, transport, id);

		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
			
		}
	}
	
	private void sendError(Message msg, MessageTransport transport, String error) {
		Message r = mf.error(msg,error);
		try {
			transport.send(r);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	
	private void sendResponse(Message r, MessageTransport transport, String id) {
		Message msg = mf.response(r);
		msg.setPayload(id);
		
		try {
			transport.send(msg);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
}
