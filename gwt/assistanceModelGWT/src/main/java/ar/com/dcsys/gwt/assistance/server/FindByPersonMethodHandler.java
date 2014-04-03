package ar.com.dcsys.gwt.assistance.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.message.shared.MessageTransport;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.justification.JustificationsManager;

@Singleton
public class FindByPersonMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindByPersonMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory af;
	private final JustificationsManager justificationsManager;
	
	@Inject
	public FindByPersonMethodHandler(AssistanceEncoderDecoder encoderDecoder,
									MessageUtils messagesFactory,
									AssistanceFactory assistanceFactory,
									JustificationsManager justificationsManager) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.af = assistanceFactory;
		this.justificationsManager = justificationsManager;
	}
	
	@Override
	public boolean handles(Method method) {
		return AssistanceMethods.findByPerson.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String params = method.getParams();
			
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
