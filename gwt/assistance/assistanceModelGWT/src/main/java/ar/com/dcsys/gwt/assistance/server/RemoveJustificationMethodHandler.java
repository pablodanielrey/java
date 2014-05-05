package ar.com.dcsys.gwt.assistance.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
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

import javax.inject.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Singleton
public class RemoveJustificationMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(RemoveGeneralJustificationDateMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsModel;
	
	@Inject
	public RemoveJustificationMethodHandler(AssistanceEncoderDecoder encoderDecoder,
											MessageUtils messagesFactory,
											AssistanceFactory assistanceFactory,
											JustificationsManager justificationsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
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
		return AssistanceMethods.removeJustification.equals(method.getName());
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
		Justification justification = ServerManagerUtils.decode(assistanceFactory,Justification.class,params);
		
		try {
			justificationsModel.remove(justification);
			sendResponse(msg, transport, msg.getId());
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
