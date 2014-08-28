package ar.com.dcsys.gwt.ws.client;

import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.ws.shared.SocketException;

/**
 * Permite enviar mensajes al servidor mediante el uso de websockets.
 * 
 * @author pablo
 *
 */

public interface WebSocket extends Transport {

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
	 * Retorna el estado interno del socket.
	 * @return
	 */
	public WebSocketState getState();
	
	
	/**
	 * Setea la url de conexion. 
	 * en el caso de no setearla se toma una por defecto.
	 * @param url
	 */
	public void setUrl(String url);
	
	
}
