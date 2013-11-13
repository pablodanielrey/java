package ar.com.dcsys.assistance;

import javax.ejb.ApplicationException;

@ApplicationException
public class AssistancePersonDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public AssistancePersonDataException(String message) {
		super(message);
	}
	
	public AssistancePersonDataException(Exception e) {
		super(e);
	}

}
