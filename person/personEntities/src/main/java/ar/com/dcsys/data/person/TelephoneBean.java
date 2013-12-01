package ar.com.dcsys.data.person;

import java.io.Serializable;

public class TelephoneBean implements Telephone, Serializable {

	private static final long serialVersionUID = 1L;

	private String number;
	private boolean mobile;

	@Override
	public String getNumber() {
		return number;
	}
	
	@Override
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public boolean isMobile() {
		return mobile;
	}
	
	@Override
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	
}
