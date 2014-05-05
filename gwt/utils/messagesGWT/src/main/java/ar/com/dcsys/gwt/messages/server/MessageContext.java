package ar.com.dcsys.gwt.messages.server;

import javax.servlet.http.HttpSession;

import ar.com.dcsys.gwt.messages.shared.Transport;

public interface MessageContext {

	Transport getTransport();
	HttpSession getHttpSession();
	
	
}
