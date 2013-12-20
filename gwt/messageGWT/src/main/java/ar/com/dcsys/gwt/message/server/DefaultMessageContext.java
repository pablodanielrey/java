package ar.com.dcsys.gwt.message.server;

import javax.servlet.http.HttpSession;

import ar.com.dcsys.gwt.message.shared.MessageTransport;

public class DefaultMessageContext implements MessageContext {

	private MessageTransport messageTransport;
	private HttpSession httpSession;
	
	
	public DefaultMessageContext() {
	}
	
	@Override
	public MessageTransport getMessageTransport() {
		return messageTransport;
	}

	@Override
	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setMessageTransport(MessageTransport messageTransport) {
		this.messageTransport = messageTransport;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	

}
