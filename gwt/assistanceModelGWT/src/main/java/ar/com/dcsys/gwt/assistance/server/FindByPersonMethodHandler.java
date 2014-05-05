package ar.com.dcsys.gwt.assistance.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.message.shared.MessageTransport;

import javax.inject.Inject;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.model.justification.JustificationsManager;

@Singleton
public class FindByPersonMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindByPersonMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder assistanceEncoderDecoder;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory af;
	private final JustificationsManager justificationsManager;
	
	@Inject
	public FindByPersonMethodHandler(AssistanceEncoderDecoder assistanceEncoderDecoder,
									PersonEncoderDecoder personEncoderDecoder,
									MessageUtils messagesFactory,
									AssistanceFactory assistanceFactory,
									JustificationsManager justificationsManager) {
		this.assistanceEncoderDecoder = assistanceEncoderDecoder;
		this.personEncoderDecoder = personEncoderDecoder;
		this.mf = messagesFactory;
		this.af = assistanceFactory;
		this.justificationsManager = justificationsManager;
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
		return AssistanceMethods.findByPerson.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		String params = method.getParams();
		List<String> lparams = ServerManagerUtils.decodeParams(params);
		
		if (lparams.size() != 3) {
			logger.log(Level.SEVERE, "Cantidad de parametros incorrectos");
			sendError(msg,transport,"Cantidad de parametros incorrectos");
		}
		try {
			List<Person> persons = personEncoderDecoder.decodePersonList(lparams.get(0));
			Date start = new Date(lparams.get(1));
			Date end = new Date(lparams.get(2));
			
			List<JustificationDate> justifications = justificationsManager.findBy(persons, start, end);
			
			//codifico los resultados
			String ljustifications = assistanceEncoderDecoder.encodeJustificationDateList(justifications);
			
			sendResponse(msg, transport, ljustifications);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}

}
