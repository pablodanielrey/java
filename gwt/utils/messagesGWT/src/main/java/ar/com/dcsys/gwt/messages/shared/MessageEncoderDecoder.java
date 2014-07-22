package ar.com.dcsys.gwt.messages.shared;

public class MessageEncoderDecoder {

	public static final String RPC = "0";
	public static final String EVENT = "1";
	public static final String BROADCAST = "00000000000000000000000000000000";			// 32 lenght
	
	/**
	 * Codifico el mensaje en id + mensage
	 * @param msg
	 * @return
	 */
	public static String encode(String subsystem, String id, String msg) throws Exception {
		if (id.length() != 32) {
			throw new Exception("id.length != 32");
		}
		if (subsystem.length() != 1) {
			throw new Exception("subsystem.length != 1");
		}
		String emsg = id + subsystem + msg;
		return emsg;
	}
	
	/**
	 * Decodifico el mensaje en id:tipo:mensaje
	 * @param msg
	 * @return
	 */
	public static String[] decode(String msg) {
		String id = msg.substring(0,32);
		String subsystem = msg.substring(32,33);
		String dmsg = msg.substring(33);
		return new String[]{id,subsystem,dmsg};
	}
		
	
}
