package ar.com.dcsys.gwt.assistance.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.justification.JustificationsManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetJustificationsMethodHandler extends AbstractMessageHandler {
	
	private static final Logger logger = Logger.getLogger(GetJustificationsMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsModel;

	@Inject
	public GetJustificationsMethodHandler(AssistanceEncoderDecoder encoderDecoder,
										MessageUtils messagesFactory,
										AssistanceFactory assistanceFactory,
										JustificationsManager justificationsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.assistanceFactory = assistanceFactory;
		this.justificationsModel = justificationsModel;
	}
	
	@Override
	public boolean handles(Method method) {
		return AssistanceMethods.getJustifications.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			List<Justification> justifications = justificationsModel.findAll();
			
			//codifico los resultados
			String ljustifications = encoderDecoder.encodeJustificationList(justifications);
			
			sendResponse(msg,transport, ljustifications);
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
