package ar.com.dcsys.exceptions;


public class PeriodException extends Exception {

	private static final long serialVersionUID = 1L;

	public PeriodException(String message) {
		super(message);
	}
	
	public PeriodException(Exception e) {
		super(e);
	}
	
}
