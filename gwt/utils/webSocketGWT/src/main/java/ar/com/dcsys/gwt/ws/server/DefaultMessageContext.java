package ar.com.dcsys.gwt.ws.server;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import ar.com.dcsys.gwt.messages.server.MessageContext;
import ar.com.dcsys.gwt.messages.shared.Transport;

public class DefaultMessageContext implements MessageContext {

	private HttpSession httpSession;
	private Session session;
	private Transport transport;
	
	@Override
	public HttpSession getHttpSession() {
		return httpSession;
	}
	
	public void setHttpSession(HttpSession s) {
		this.httpSession = s;
	}

	public Session getWsSession() {
		return session;
	}

	public void setWsSession(Session s) {
		this.session = s;
	}

	@Override
	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	
	
}
