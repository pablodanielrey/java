package ar.com.dcsys.gwt.message.server.handlers;

import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.Method;

public interface MethodHandler {

	public boolean handles(Method method);
	public void handle(MessageContext context, Message msg, Method method);
	
}
