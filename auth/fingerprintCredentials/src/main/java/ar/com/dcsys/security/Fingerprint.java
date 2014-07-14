package ar.com.dcsys.security;

import java.io.Serializable;

public class Fingerprint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String personId;
	private String codification;
	private byte[] template;
	private Finger finger;
	private String algorithm;
	
	public Fingerprint() {
	}
	
	public Fingerprint(Finger finger, String algorithm, String codification, byte[] template) {
		this.template = template;
		this.codification = codification;
		this.finger = finger;
		this.algorithm = algorithm;
	}

	public void setCodification(String codification) {
		this.codification = codification;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

	public void setFinger(Finger finger) {
		this.finger = finger;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public byte[] getTemplate() {
		return template;
	}

	public String getCodification() {
		return codification;
	}

	public Finger getFinger() {
		return finger;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

}
