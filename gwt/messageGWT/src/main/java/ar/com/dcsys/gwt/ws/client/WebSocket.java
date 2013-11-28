package ar.com.dcsys.gwt.ws.client;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.ws.shared.SocketException;

/**
 * Interface para enviar mensajes desde el cliente hacia el servidor en formato de @see ar.com.dcsys.gwt.message.shared.Message
 * 
 * @author pablo
 *
 */

public interface WebSocket {

	/**
	 * Conecta el cliente con el servidor.
	 * @throws SocketException
	 */
	public void open() throws SocketException;
	
	
	
	/**
	 * Cierra la conexión con el servidor.
	 * @throws SocketException
	 */
	public void close() throws SocketException;
	
	/**
	 * Envía un mensaje hacia el servidor y en forma asincrónica llama a los métodos de rec cuando el servidor retora una respuesta.
	 * @param msg
	 * @param rec
	 */
	public void send(Message msg, WebSocketReceiver rec);

	
}
