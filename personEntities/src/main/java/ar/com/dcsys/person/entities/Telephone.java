package ar.com.dcsys.person.entities;

import java.io.Serializable;

public class Telephone implements Serializable {

	private static final long serialVersionUID = 1L;

	private String number;
	private boolean mobile;

	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public boolean isMobile() {
		return mobile;
	}
	
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	
}
