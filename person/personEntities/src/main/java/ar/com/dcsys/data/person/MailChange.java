package ar.com.dcsys.data.person;

import java.io.Serializable;

public class MailChange implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String personId;
	private Mail mail;
	private boolean confirmed;
	private String token;
	
	
	public Mail getMail() {
		return mail;
	}

	
	public void setMail(Mail mail) {
		this.mail = mail;
	}

	
	public boolean isConfirmed() {
		return confirmed;
	}

	
	public void setConfirmed(boolean v) {
		this.confirmed = v;
	}

	
	public String getToken() {
		return token;
	}

	
	public void setToken(String token) {
		this.token = token;
	}

	
	public String getPersonId() {
		return personId;
	}

	
	public void setPersonId(String personId) {
		this.personId = personId;
	}

}
