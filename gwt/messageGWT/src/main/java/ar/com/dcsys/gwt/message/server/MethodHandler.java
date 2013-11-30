package ar.com.dcsys.gwt.message.server;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.Method;

public interface MethodHandler {

	public boolean handles(Method method);
	public void handle(Message msg, Method method, MessageTransport transport);
	
}
