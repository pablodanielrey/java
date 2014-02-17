package ar.com.dcsys.data.person;

import java.io.Serializable;

public class MailChangeBean implements MailChange, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String personId;
	private Mail mail;
	private boolean confirmed;
	private String token;
	
	@Override
	public Mail getMail() {
		return mail;
	}

	@Override
	public void setMail(Mail mail) {
		this.mail = mail;
	}

	@Override
	public boolean isConfirmed() {
		return confirmed;
	}

	@Override
	public void setConfirmed(boolean v) {
		this.confirmed = v;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String getPersonId() {
		return personId;
	}

	@Override
	public void setPersonId(String personId) {
		this.personId = personId;
	}

}
