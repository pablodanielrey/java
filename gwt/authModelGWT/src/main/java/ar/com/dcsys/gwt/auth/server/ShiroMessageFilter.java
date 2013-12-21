package ar.com.dcsys.gwt.auth.server;

import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import ar.com.dcsys.auth.shiro.SecurityUtils;
import ar.com.dcsys.gwt.manager.server.AbstractMessageFilter;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;

public class ShiroMessageFilter extends AbstractMessageFilter {

	/**
	 * Seteo el subject de shiro dentro del ThreadContext.
	 * asi es posible obtenerlo usando SecurityUtils.getSubject()
	 * como normalmente se haría en alguna aplicacion controlada por shiro.
	 */
	@Override
	public void filter(MessageContext ctx, Message msg) {

		HttpSession session = ctx.getHttpSession();
		Subject subject = SecurityUtils.getSubject(session);
		ThreadContext.bind(subject);		
		
	}

}
