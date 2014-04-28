package ar.com.dcsys.gwt.messages.shared;


/**
 * Define una interface para el tratamiento de las respuestas de las llamadas a métodos desde el cliente hacia el servidor.
 * 
 * @author pablo
 *
 */

public interface TransportReceiver {

	/**
	 * En caso de éxito se llama a onSuccess.
	 * 
	 * @param message
	 */
	public void onSuccess(String message);
	
	/**
	 * En caso de error se llama a onFailure.
	 * @param error
	 */
	public void onFailure(String error);

	
}
