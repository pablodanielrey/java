package ar.com.dcsys.gwt.message.server;

import javax.servlet.http.HttpSession;

import ar.com.dcsys.gwt.message.shared.MessageTransport;

public interface MessageContext {

	public MessageTransport getMessageTransport();
	public HttpSession getHttpSession();
	
}
