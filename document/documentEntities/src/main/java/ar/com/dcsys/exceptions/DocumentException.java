package ar.com.dcsys.exceptions;


public class DocumentException extends Exception {

	private static final long serialVersionUID = 1L;

	public DocumentException(String message) {
		super(message);
	}
	
	public DocumentException(Exception c) {
		super(c);
	}
	
	public DocumentException(Throwable c) {
		super(c);
	}
	
}
