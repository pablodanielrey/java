package ar.com.dcsys.gwt.message.shared;

public interface MessagesFactory {

	public Message method(String function);
	public Message method(String function, String params);
	
	public Method method(Message msg);
	
}
