package ar.com.dcsys.mail;

public class MailException extends Exception {

	private static final long serialVersionUID = 1L;

	public MailException() {
	}

	public MailException(String message) {
		super(message);
	}
	
	public MailException(Throwable e) {
		super(e);
	}
	
}
