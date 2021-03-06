package ar.com.dcsys.gwt.auth.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.gwt.auth.shared.AuthMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.auth.AuthManager;


public class IsAuthenticatedMessageHandler extends AbstractMessageHandler {

	public static final Logger logger = Logger.getLogger(IsAuthenticatedMessageHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ServerManagerUtils managerUtils;
	private final AuthManager authManager;

	@Inject
	public IsAuthenticatedMessageHandler(MessageUtils messageUtils,
										 ServerManagerUtils managerUtils,
										 AuthManager authManager) {
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
		this.authManager = authManager;
	}
	
	@Override
	public boolean handles(Method method) {
		return (AuthMethods.isAuthenticated.equals(method.getName()));
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
				
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			boolean authenticated = authManager.isAuthenticated();

			String json = managerUtils.encodeBoolean(authenticated);
			sendResponse(msg, transport, json);
	
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
		return messageUtils;
	}

}
