package ar.com.dcsys.person.entities;

import java.io.Serializable;

public class Mail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mail;
	private boolean primary = false;
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
}
