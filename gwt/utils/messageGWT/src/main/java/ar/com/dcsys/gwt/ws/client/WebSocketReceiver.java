package ar.com.dcsys.gwt.ws.client;

import ar.com.dcsys.gwt.message.shared.Message;

/**
 * Define una interface para el tratamiento de las respuestas de las llamadas a métodos desde el cliente hacia el servidor.
 * 
 * @author pablo
 *
 */

public interface WebSocketReceiver {

	/**
	 * En caso de éxito se llama a onSuccess.
	 * Esto implica el envío del mensaje exitoso y el procesamiento del lado del servidor
	 * exitoso tambien.
	 * 
	 * @param message, mensaje de respuesta desde el servidor hacia el cliente.
	 */
	public void onSuccess(Message message);
	
	/**
	 * En caso de error se llama a onFailure.
	 * @param t, excepción encontrada en el servidor o cliente cuando se enviaba el mensaje.
	 */
	public void onFailure(Throwable t);
	
}
