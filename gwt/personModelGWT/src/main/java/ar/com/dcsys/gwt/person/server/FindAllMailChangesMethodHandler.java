package ar.com.dcsys.gwt.person.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.MailChangesManager;

@Singleton
public class FindAllMailChangesMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindAllMailChangesMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final PersonFactory pf;
	private final MailChangesManager mailChangesManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
	
	@Inject
	public FindAllMailChangesMethodHandler(PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory, 
									  PersonFactory personFactory,
									  MailChangesManager mailChangesManager) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.pf = personFactory;
		this.mailChangesManager = mailChangesManager;
	}
	
	@Override
	public boolean handles(Method method) {
		return PersonMethods.findAllMailChanges.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {

		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String params = method.getParams();
			Person person = ManagerUtils.decode(pf,Person.class,params);

			List<MailChange> changes = mailChangesManager.findAllBy(person);
			
			String list = encoderDecoder.encodeMailChangeList(changes);
			sendResponse(msg, transport, list);
		
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
	}
	
}
