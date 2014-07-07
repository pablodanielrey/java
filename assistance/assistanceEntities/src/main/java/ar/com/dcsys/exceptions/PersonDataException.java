package ar.com.dcsys.exceptions;

public class PersonDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersonDataException(String message) {
		super(message);
	}
	
	public PersonDataException(Exception e) {
		super(e);
	}
	
}
