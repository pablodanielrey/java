package ar.com.dcsys.gwt.manager.server.handler;

import ar.com.dcsys.gwt.manager.shared.message.Message;
import ar.com.dcsys.gwt.messages.server.MessageContext;

public interface MethodHandler {
	
	public boolean process(String id, Message msg, MessageContext ctx);
	
}
