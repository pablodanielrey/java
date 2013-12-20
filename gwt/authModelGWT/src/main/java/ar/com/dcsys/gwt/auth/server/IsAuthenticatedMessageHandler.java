package ar.com.dcsys.gwt.auth.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.util.WebUtils;

import ar.com.dcsys.gwt.auth.shared.AuthMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;


public class IsAuthenticatedMessageHandler extends AbstractMessageHandler {

	public static final Logger logger = Logger.getLogger(IsAuthenticatedMessageHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;

	@Inject
	public IsAuthenticatedMessageHandler(MessageUtils messageUtils,
										 ManagerUtils managerUtils) {
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
	}
	
	@Override
	public boolean handles(Method method) {
		return (AuthMethods.isAuthenticated.equals(method.getName()));
	}

	
	private Subject getSubject(MessageContext ctx) {
		
		/*
		// esta almacenado anteriormente por StoreServlet. 
		
		HttpSession session = ctx.getHttpSession();
		Object o = session.getAttribute(AuthConfig.SUBJECT);
		if (o == null) {
			return null;
		} else {
			return (Subject)o;
		}
		*/
		
		HttpSession session = ctx.getHttpSession();
		ServletContext scontext = session.getServletContext();
		WebEnvironment environment = WebUtils.getWebEnvironment(scontext);
		WebSecurityManager securityManager = environment.getWebSecurityManager();
		Subject subject = new Subject.Builder(securityManager).sessionId(session.getId()).buildSubject();
		
		return subject;
	}
	
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			Subject subject = getSubject(ctx);
			boolean authenticated = false;
			if (subject != null) {
				authenticated = subject.isAuthenticated();
			}
			
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
