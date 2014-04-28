package ar.com.dcsys.gwt.messages.shared;


/**
 * Permite enviar mensajes al servidor mediante el uso de websockets.
 * 
 * @author pablo
 *
 */

public interface Transport {

	/**
	 * Envía un mensaje hacia el servidor y en forma asincrónica llama a los métodos de rec cuando el servidor retora una respuesta.
	 * @param msg
	 * @param rec
	 */
	public void send(String msg, TransportReceiver rec);

	
}
