package ar.com.dcsys.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class MapauException extends Exception {

	private static final long serialVersionUID = 1L;

	public MapauException(Exception e) {
		super(e);
	}
	
	public MapauException(String message) {
		super(message);
	}
	
}
