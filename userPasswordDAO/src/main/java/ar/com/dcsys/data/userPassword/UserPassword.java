package ar.com.dcsys.data.userPassword;

import java.io.Serializable;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.security.UserPasswordCredentials;

public class UserPassword implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private Person person;
	private UserPasswordCredentials credentials;
	
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}	
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public UserPasswordCredentials getCredentials() {
		return credentials;
	}
	
	public void setCredentials(UserPasswordCredentials credentials) {
		this.credentials = credentials;
	}
	
}
