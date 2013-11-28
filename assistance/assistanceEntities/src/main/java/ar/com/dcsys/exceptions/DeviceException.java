package ar.com.dcsys.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class DeviceException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeviceException(String message) {
		super(message);
	}
	
	public DeviceException(Exception e) {
		super(e);
	}
}
