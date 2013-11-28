package ar.com.dcsys.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class PersonNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersonNotFoundException() {
		
	}
	
	
	
}
