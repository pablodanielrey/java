package ar.com.dcsys.data.fingerprint;

import java.io.Serializable;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.security.FingerprintCredentials;

public class Fingerprint implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private Person person;
	private FingerprintCredentials fingerprint;
	
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
	
	public FingerprintCredentials getFingerprint() {
		return fingerprint;
	}
	
	public void setFingerprint(FingerprintCredentials fingerprint) {
		this.fingerprint = fingerprint;
	}
	
}
