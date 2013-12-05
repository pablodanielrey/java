package ar.com.dcsys.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class FilterException extends Exception {

	private static final long serialVersionUID = 1L;

	public FilterException(Throwable e) {
		super(e);
	}
	
	public FilterException(String message) {
		super(message);
	}
	
	public FilterException() {
		super();
	}
	
}
