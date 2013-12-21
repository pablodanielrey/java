package ar.com.dcsys.gwt.auth.server;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import ar.com.dcsys.auth.shiro.SecurityUtils;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;

/**
 * Clase base que debe ser implementada antes de cualquier handler que deba ejecutar codigo relacionado con la seguridad.
 * 
 * @author pablo
 *
 */
public abstract class ShiroMessageHandler extends AbstractMessageHandler {

	@Override
	public abstract boolean handles(Method method);

	@Override
	protected abstract Logger getLogger();
	
	@Override
	protected abstract MessageUtils getMessageUtils();
	
	/**
	 * Setea el subject en ThreadContext para que pueda ser obtenido en las diferentes capas del modelo 
	 * siguiente usando SecurityUtils.getSubject();
	 * @param ctx
	 */
	private void setSubject(MessageContext ctx) {
		
		HttpSession session = ctx.getHttpSession();
		Subject subject = SecurityUtils.getSubject(session);
		ThreadContext.bind(subject);
		
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		setSubject(ctx);
	}

}
