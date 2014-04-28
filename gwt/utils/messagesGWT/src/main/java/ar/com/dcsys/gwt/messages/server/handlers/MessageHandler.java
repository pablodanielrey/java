package ar.com.dcsys.gwt.messages.server.handlers;

import ar.com.dcsys.gwt.messages.server.MessageContext;

public interface MessageHandler {

	public boolean handle(String id, String msg, MessageContext ctx);
	
}
