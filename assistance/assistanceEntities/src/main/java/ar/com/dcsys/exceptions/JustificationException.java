package ar.com.dcsys.exceptions;


public class JustificationException extends Exception {

	private static final long serialVersionUID = 1L;

	public JustificationException(String message) {
		super(message);
	}
	
	public JustificationException(Exception e) {
		super(e);
	}
	
}
