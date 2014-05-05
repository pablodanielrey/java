package ar.com.dcsys.gwt.messages.shared;


/**
 * Permite enviar mensajes al servidor mediante el uso de websockets.
 * 
 * @author pablo
 *
 */

public interface Transport {

	/**
	 * Envía un mensaje hacia el servidor y en forma asincrónica.
	 * Se autogenera el id del mensaje.
	 * @param msg
	 * @param rec
	 */
	public void send(String msg, TransportReceiver rec);

	/**
	 * Envía un mensaje hacia el servidor y en forma asincrónica.
	 * 
	 * @param msg
	 * @param rec
	 */
	public void send(String id, String msg, TransportReceiver rec);

	
}
