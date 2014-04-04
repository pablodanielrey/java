package ar.com.dcsys.exceptions;


public class DocumentNotFoundException extends DocumentException {

	private static final long serialVersionUID = 1L;

	public DocumentNotFoundException(String message) {
		super(message);
	}
	
	public DocumentNotFoundException(Exception c) {
		super(c);
	}
	
	public DocumentNotFoundException(Throwable c) {
		super(c);
	}
	
}
