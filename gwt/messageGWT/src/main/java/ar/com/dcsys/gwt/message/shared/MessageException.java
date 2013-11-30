package ar.com.dcsys.gwt.message.shared;

public class MessageException extends Exception {

	private static final long serialVersionUID = 1L;

	public MessageException() {
		super();
	}

	public MessageException(Throwable t) {
		super(t);
	}
	
	public MessageException(String m) {
		super(m);
	}	
}
