package ar.com.dcsys.exceptions;


public class DeviceException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeviceException(String message) {
		super(message);
	}
	
	public DeviceException(Exception e) {
		super(e);
	}
}
