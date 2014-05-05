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
	 * Setea los campos necesarios para indicar que un mensaje es respuessta a otro mensaje recibido.
	 * Esto es necesario cuando por ejemplo generamos un error y este es respuesta a otro mensaje.
	 * por ejemplo :
	 * 		Message error = MessageUtils.error("esto es un error");
	 * 		MessageUtils.setAsResponse(original, error);
	 * 
	 * es equivalente a hacer :
	 * 
	 * 		Message error = MessageUtils.error(original,"esto es un error");
	 * 
	 * NO setea el tipo de respuesta!!!, ya que eso depende del mesnaje a enviar. en el caso de error, o en el caso de un retorno de una función.
	 * 
	 * @param original, mensaje original recibido al cual se esta respondiendo con otro mensaje
	 * @param response, mensaje de respuesta.
	 */
	public void setAsResponse(Message original, Message response);
	
	
	/**
	 * REtorna un mensaje de tipo evento con el payload pasado por parámetro para ser enviado por el transport.
	 * @param name
	 * @param payload
	 * @return
	 */
	public Message event(String name, String payload);
	
	
	/**
	 * Retorna el método encapsulado dentro dle mesnaje.
	 * @param msg, mensaje que contiene adentro el metodo.
	 * s@return
	 */
	public Method method(Message msg);

	
	public Event event(Message msg);
	
}
