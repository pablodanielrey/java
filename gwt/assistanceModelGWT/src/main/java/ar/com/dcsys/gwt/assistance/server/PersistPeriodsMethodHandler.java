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
import ar.com.dcsys.model.period.PeriodsManager;

import javax.inject.Inject;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

@Singleton
public class PersistPeriodsMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(PersistGeneralJustificationDateMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final PeriodsManager periodsManager;
	
	@Inject
	public PersistPeriodsMethodHandler(AssistanceEncoderDecoder encoderDecoder,
									   MessageUtils messagesFactory,
									   AssistanceFactory assistanceFactory,
									   PeriodsManager periodsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.assistanceFactory = assistanceFactory;
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
		return AssistanceMethods.persistPeriods.equals(method.getName());
	}
	
	private void sendEvent(MessageTransport transport, String id) {
		Message msg = mf.event(AssistanceMethods.periodModifiedEvent, id);
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
		List<String> lparams = ServerManagerUtils.decodeParams(params);
		
		if (lparams.size() != 2) {
			logger.log(Level.SEVERE, "Cantidad de parametros incorrectos");
			sendError(msg,transport,"Cantidad de parametros incorrectos");
		}
		
		try {
			Person person = ServerManagerUtils.decode(assistanceFactory,Person.class,lparams.get(0));
			PeriodAssignation period = ServerManagerUtils.decode(assistanceFactory,PeriodAssignation.class,lparams.get(1));
			
			//verifico que no exista
			PeriodAssignation pa = periodsManager.findBy(person, period.getStart(), period.getType());
			if (pa != null) {
				return;
			}
			period.setPerson(person);
			periodsManager.persist(period);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
		sendEvent(transport, msg.getId());
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
