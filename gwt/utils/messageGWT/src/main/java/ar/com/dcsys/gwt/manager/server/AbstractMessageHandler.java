package ar.com.dcsys.gwt.manager.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;

import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.server.handlers.MethodHandler;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;

public abstract class AbstractMessageHandler implements MethodHandler {

	@Override
	public abstract boolean handles(Method method);

	@Override
	public abstract void handle(MessageContext ctx, Message msg, Method method);

	/**
	 * Retorna el logger asignado para poder loggear información y errores.
	 * @return
	 */
	protected abstract Logger getLogger();
	
	protected abstract MessageUtils getMessageUtils();
	
	
	/**
	 * Se registra como handler cuando es llamado por el evento disparado por CDI
	 * @param mh
	 */
	public void register(@Observes MessageHandlers mh) {
		mh.addHandler(this);
	}
	
	/**
	 * Envía una respuesta de error al origen del mensaje msg. usando el transport pasado por parámetro.
	 * @param msg, mensaje a responder con error
	 * @param transport, transporte a usar para enviar el mesanje de error.
	 * @param error, error a enviar.
	 */
	protected void sendError(Message msg, MessageTransport transport, String error) {
		Message r = getMessageUtils().error(msg,error);
		try {
			transport.send(r);
		} catch (MessageException e) {
			getLogger().log(Level.SEVERE,e.getMessage(),e);
		}
	}	
	
	/**
	 * Envía una respuesta al mensaje r. usando el transport pasado como parámetro.
	 * @param r, mensaje a responder.
	 * @param transport, transporte a usar para enviar la respuesta.
	 * @param payload, datos de payload que van dentro del mesnaje de respuesta.
	 */
	protected void sendResponse(Message r, MessageTransport transport, String payload) {
		Message msg = getMessageUtils().response(r);
		msg.setPayload(payload);
		try {
			transport.send(msg);
		} catch (MessageException e) {
			getLogger().log(Level.SEVERE,e.getMessage(),e);
		}
	}	

}
