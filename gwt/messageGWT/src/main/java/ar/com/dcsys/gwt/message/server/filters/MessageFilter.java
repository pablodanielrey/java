package ar.com.dcsys.gwt.message.server.filters;

import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;


public interface MessageFilter {

	public void filter(MessageContext ctx, Message msg);
	
}
