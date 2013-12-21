package ar.com.dcsys.gwt.person.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.PersonsManager;

@Singleton
public class FindPersonByDniMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindPersonByDniMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final PersonFactory pf;
	private final PersonsManager personsModel;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
		
	
	@Inject
	public FindPersonByDniMethodHandler(PersonEncoderDecoder encoderDecoder,
									  MessageUtils messagesFactory, 
									  PersonFactory personFactory,
									  PersonsManager personsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.pf = personFactory;
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
		return PersonMethods.findByDni.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {

		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String dni = method.getParams();
			Person person = personsModel.findByDni(dni);
			if (person == null) {
				sendResponse(msg, transport, null);
			} else {
				String lpersons = ManagerUtils.encode(pf, Person.class,person);
				sendResponse(msg, transport, lpersons);
			}
		
		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
	}
	

}
