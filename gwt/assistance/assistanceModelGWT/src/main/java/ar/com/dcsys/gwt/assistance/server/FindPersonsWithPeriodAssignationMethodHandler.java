package ar.com.dcsys.gwt.assistance.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.model.period.PeriodsManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FindPersonsWithPeriodAssignationMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindPersonsWithPeriodAssignationMethodHandler.class.getName());
	
	private final PersonEncoderDecoder personEncoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final PeriodsManager periodsManager;
	
	@Inject
	public FindPersonsWithPeriodAssignationMethodHandler(PersonEncoderDecoder personEncoderDecoder,
														 MessageUtils messagesFactory,
														 AssistanceFactory assistanceFactory,														
														 PeriodsManager periodsModel) {
		this.personEncoderDecoder = personEncoderDecoder;
		this.mf = messagesFactory;
		this.assistanceFactory = assistanceFactory;
		this.periodsManager = periodsModel;
	}
	
	@Override
	public boolean handles(Method method) {
		return AssistanceMethods.findPersonsWithPeriodAssignation.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			List<Person> persons = periodsManager.findPersonsWithPeriodAssignations();
			
			//codifico los resultados
			String lpersons = personEncoderDecoder.encodePersonList(persons);
			
			sendResponse(msg, transport, lpersons);
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
