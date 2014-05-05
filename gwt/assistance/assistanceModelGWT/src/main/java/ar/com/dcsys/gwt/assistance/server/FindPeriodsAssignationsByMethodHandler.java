package ar.com.dcsys.gwt.assistance.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.model.period.PeriodsManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.enterprise.event.Observes;

@Singleton
public class FindPeriodsAssignationsByMethodHandler extends AbstractMessageHandler {
	
	private static final Logger logger = Logger.getLogger(FindPeriodsAssignationsByMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private AssistanceFactory af;
	private final PeriodsManager periodsManager;
	
	@Inject
	public FindPeriodsAssignationsByMethodHandler(AssistanceEncoderDecoder encoderDecoder,
												  MessageUtils messagesFactory,
												  AssistanceFactory assistanceFactory,
												  PeriodsManager periodsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.af = assistanceFactory;
		this.periodsManager = periodsModel;
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
		return AssistanceMethods.findPeriodsAssignationsBy.equals(method.getName());
	}

	
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		String params = method.getParams();
		Person person = ServerManagerUtils.decode(af, Person.class, params);
		
		try {
			List<PeriodAssignation> periods = periodsManager.findAll(person);
			
			//codifico los resultados
			String lperiods = encoderDecoder.encodePeriodAssignationList(periods);
			
			sendResponse(msg, transport, lperiods);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(),e);
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
