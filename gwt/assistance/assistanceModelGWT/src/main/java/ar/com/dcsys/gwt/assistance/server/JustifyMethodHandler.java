package ar.com.dcsys.gwt.assistance.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
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
import ar.com.dcsys.model.justification.JustificationsManager;

import javax.inject.Inject;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

@Singleton
public class JustifyMethodHandler extends AbstractMessageHandler {
	
	private static final Logger logger = Logger.getLogger(JustifyMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsModel;

	@Inject
	public JustifyMethodHandler(AssistanceEncoderDecoder encoderDecoder,
								MessageUtils mf,
								AssistanceFactory assistanceFactory,
								JustificationsManager justificationsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = mf;
		this.assistanceFactory = assistanceFactory;
		this.justificationsModel = justificationsModel;
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
		return AssistanceMethods.justify.equals(method.getName());
	}
	
	private void sendEvent(MessageTransport transport, String id) {
		Message msg = mf.event(AssistanceMethods.justificationModifiedEvent, id);
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
		
		if (!(lparams.size() >= 4 && lparams.size() < 6)) {
			logger.log(Level.SEVERE, "Cantidad de parametros incorrectos");
			sendError(msg,transport,"Cantidad de parametros incorrectos");
		}
		
		try {
			Person person = ServerManagerUtils.decode(assistanceFactory, Person.class, lparams.get(0));
			Date start = new Date(lparams.get(1));
			Date end = new Date(lparams.get(2));
			Justification justification = ServerManagerUtils.decode(assistanceFactory, Justification.class, lparams.get(3));
			String notes = "";
			if (lparams.size() == 5) {
				notes = lparams.get(4);
			}
			
			justificationsModel.justify(person, start, end, justification, false, notes);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
		sendEvent(transport,msg.getId());
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
