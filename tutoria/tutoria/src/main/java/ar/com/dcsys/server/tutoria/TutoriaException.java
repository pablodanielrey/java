package ar.com.dcsys.server.tutoria;

public class TutoriaException extends Exception {

	private static final long serialVersionUID = 1L;

	public TutoriaException(String message) {
		super(message);
	}
	
	public TutoriaException(Exception e) {
		super(e);
	}
	
}
