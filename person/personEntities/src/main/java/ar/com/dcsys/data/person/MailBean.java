package ar.com.dcsys.data.person;

import java.io.Serializable;

public class MailBean implements Mail, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mail;
	private boolean primary = false;
	
	@Override
	public String getMail() {
		return mail;
	}

	@Override
	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}

	@Override
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
}
