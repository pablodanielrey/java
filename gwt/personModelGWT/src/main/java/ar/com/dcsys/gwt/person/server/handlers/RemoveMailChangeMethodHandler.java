package ar.com.dcsys.gwt.person.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.mail.MailData;
import ar.com.dcsys.mail.MailsManager;
import ar.com.dcsys.model.MailChangesManager;

@Singleton
public class RemoveMailChangeMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(RemoveMailChangeMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final MailChangesManager mailChangesModel;
	private final PersonFactory personFactory;
	private final MailsManager mailsManager;
	private final MailData mailData;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
		
	
	@Inject
	public RemoveMailChangeMethodHandler(PersonFactory personFactory,
									  PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory,
									  MailChangesManager mailChangesManager,
									  MailsManager mailsManager,
									  MailData mailData) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.mailChangesModel = mailChangesManager;
		this.personFactory = personFactory;
		this.mailsManager = mailsManager;
		this.mailData = mailData;
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
		return PersonMethods.removeMailChange.equals(method.getName());
	}
	
	private void sendEvent(MessageTransport transport, String id) {
		Message msg = mf.event(PersonMethods.mailChangeModifiedEvent, id);
		try {
			transport.send(msg);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		String params = method.getParams();
		MailChange mailChange = ServerManagerUtils.decode(personFactory,MailChange.class,params);
		
		try {
			if (mailChange.getToken() == null) {
				mailChangesModel.remove(mailChange.getMail().getMail());
				sendResponse(msg, transport, msg.getId());
			} else {
				mailChangesModel.remove(mailChange);
				sendResponse(msg, transport, msg.getId());
			}
			
			
		} catch (Exception e) {
//		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}

		sendEvent(transport, msg.getId());
		
	}

	/* ejemplo de como enviar un mail.
	private void sendMail(Person person, MailChange mailChange) throws MailException {
		String from = mailData.from();
		String to = mailChange.getMail().getMail();
		
		String token = mailChange.getToken();
		if (token == null) {
			throw new MailException("token == null");
		}
		
		String subject = "Confirmación de cambio de correo requerida";
		String body = "Debe confirmar haciendo click en el siguiente link,\n" + 
					  "https://fce.econo.unlp.edu.ar/personGWT/mailChanges?t=" + token + "\n" +
					  "el cambio de cuenta de correo para la persona con dni : " + person.getDni();
		
		mailsManager.sendMail(from, to, subject, body);
	}
	*/
	
}
