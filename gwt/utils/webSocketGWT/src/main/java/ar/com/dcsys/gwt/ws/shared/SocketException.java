package ar.com.dcsys.gwt.ws.shared;

public class SocketException extends Exception {

	private static final long serialVersionUID = 1L;

	public SocketException() {
	}
	
	public SocketException(String message) {
		super(message);
	}
	
	public SocketException(Exception t) {
		super(t);
	}
	
}
