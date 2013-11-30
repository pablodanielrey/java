package ar.com.dcsys.gwt.message.shared;

public interface MessagesFactory {

	////////// creación ////
	
	public Message method(String function);
	public Message method(String function, String params);
	
	public Message error(String error);
	
	public Message response(Message request);
	
	//////// decodificacion //////////
	
	public Method method(Message msg);
	
}
