package ar.com.dcsys.exceptions;


public class PersonException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersonException(String message) {
		super(message);
	}
	
	public PersonException(Exception c) {
		super(c);
	}
	
	public PersonException(Throwable c) {
		super(c);
	}
	
}
