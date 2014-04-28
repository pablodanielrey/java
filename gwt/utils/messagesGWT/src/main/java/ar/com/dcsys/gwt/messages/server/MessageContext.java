package ar.com.dcsys.gwt.messages.server;

import javax.servlet.http.HttpSession;

public interface MessageContext {

	String sessionId();
	HttpSession getHttpSession();
	
}
