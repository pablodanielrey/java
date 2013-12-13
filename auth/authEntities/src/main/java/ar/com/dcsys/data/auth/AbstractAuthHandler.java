package ar.com.dcsys.data.auth;

import javax.enterprise.event.Observes;

public abstract class AbstractAuthHandler implements AuthHandler {

	/**
	 * Escucha por el evento de cdi y se registra dentro de la colección de handlers.
	 * @param hs, collección de handlers
	 */
	public void register(@Observes AuthHandlers hs) {
		hs.addHandler(this);
	}
	
}
