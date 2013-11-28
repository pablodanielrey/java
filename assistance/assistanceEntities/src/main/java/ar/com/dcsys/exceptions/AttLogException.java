package ar.com.dcsys.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class AttLogException extends Exception {

	private static final long serialVersionUID = 1L;

	public AttLogException(String message) {
		super(message);
	}
	
	public AttLogException(Exception e) {
		super(e);
	}
	
}
