package ar.com.dcsys.gwt.message.shared;

public interface MessageUtils {

	/**
	 * Retorna un mensaje que tiene adentro una llamada a método.
	 * @param function, nombre de la funcion a llamar
	 * @return
	 */
	public Message method(String function);
	
	/**
	 * Retorna un mesnaje que tiene adentro una llamada a un método.
	 * @param function, metodo a invocar
	 * @param params, parámetros serializados a guardar dentro del mensaje
	 * @return
	 */
	public Message method(String function, String params);
	
	/**
	 * Retorna un mensaje de error en respuesta a un mesaje pasado como parámetro.
	 * @param msg, mensaje a responder
	 * @param error, error indicado en el mensaje
	 * @return
	 */
	public Message error(Message msg, String error);
	
	/**
	 * Retorna un mensaje de error, el mensaje no tiene ni id, ni sessionId, por lo que nos e sabe a quien mandarlo.
	 * Estos parámetros deben ser proporcionados antes de poder enviar el mensaje.
	 * @param error, error indicado en el mesnaje.
	 * @return
	 */
	public Message error(String error);
	
	/**
	 * Retorna un mesnaje que representa una respuesta a un mensaje original.
	 * @param request, mensaje original.
	 * @return
	 */
	public Message response(Message request);
	
	
	/**
	 * Retorna el método encapsulado dentro dle mesnaje.
	 * @param msg, mensaje que contiene adentro el metodo.
	 * @return
	 */
	public Method method(Message msg);
	
}
