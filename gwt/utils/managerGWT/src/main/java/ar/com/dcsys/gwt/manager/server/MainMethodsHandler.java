package ar.com.dcsys.gwt.manager.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ar.com.dcsys.gwt.manager.server.handler.MethodHandler;
import ar.com.dcsys.gwt.manager.server.handler.MethodHandlersDetection;
import ar.com.dcsys.gwt.messages.server.MessageContext;
import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;
import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;

public class MainMethodsHandler implements MessageHandler {

	private static Logger logger = Logger.getLogger(MainMethodsHandler.class.getName());
	
	/**
	 * Se registra dentro de los habdlers de los mensajes.
	 * Asi cuanod llegan mensajes es llamado para manejarlos.
	 * Si detecta mensajes válidos entonces llama a cada uno de los handlers de los métodos.
	 * @param handlers
	 */
	private void register(@Observes HandlersContainer<MessageHandler> handlers) {
		handlers.add(this);
	}
	
	private final List<MethodHandler> handlers = new ArrayList<>();
	
	@Inject
	public MainMethodsHandler(MethodHandlersDetection mhd) {
		List<MethodHandler> handlers = mhd.discover();
		for (MethodHandler mh : handlers) {
			logger.info("Registering Method Handler : " + mh.getClass().getName());
		}
		
		this.handlers.addAll(handlers);
	}
	
	
	@Override
	public boolean handle(String id, String msg, MessageContext ctx) {
		
		return false;
	}

}
