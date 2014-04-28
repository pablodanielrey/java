package ar.com.dcsys.gwt.messages.shared;

public class MessageEncoderDecoder {

	/**
	 * Codifico el mensaje en id + mensage
	 * @param msg
	 * @return
	 */
	public static String encode(String id, String msg) throws Exception {
		if (id.length() != 32) {
			throw new Exception("id.length != 32");
		}
		String emsg = id + msg;
		return emsg;
	}
	
	/**
	 * Decodifico el mensaje en id:tipo:mensaje
	 * @param msg
	 * @return
	 */
	public static String[] decode(String msg) {
		String id = msg.substring(0,32);
		String dmsg = msg.substring(32);
		return new String[]{id,dmsg};
	}
		
	
}
