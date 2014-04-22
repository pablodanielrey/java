/**
 * Escucha eventos disparados por CDI que contienen mensajes y los envía en el caso de que se haya registrado algun transport.
 * 
 */

package ar.com.dcsys.gwt.ws.server;

import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;

@Singleton
public class EventsDispatcher {

	private final Logger logger = Logger.getLogger(EventsDispatcher.class.getName());
	private MessageTransport transport = null;
	
	public void onTransport(@Observes MessageTransport transport) {
		this.transport = transport;
		logger.info("Transport nuevo obtenido");
	}
	
	
	// escucha mensajes enviados por eventos de CDI
	public void onMessage(@Observes Message msg) {
		if (transport == null) {
			logger.info("MessageTransport == null");
			return;
		}
		
		try {
			transport.send(msg);
			
		} catch (MessageException e) {
			logger.severe(e.getMessage());
		}
	}
	
}
