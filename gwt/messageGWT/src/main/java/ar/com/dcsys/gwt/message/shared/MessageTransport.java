package ar.com.dcsys.gwt.message.shared;


public interface MessageTransport {

	public void send(Message msg) throws MessageException;
	
}
