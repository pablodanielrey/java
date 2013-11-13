package ar.com.dcsys.data.auth;

import java.io.Serializable;

import ar.com.dcsys.group.entities.Group;
import ar.com.dcsys.person.entities.Person;

public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	
	public Permission() {
		
	}
	
	private Person person;
	private String profile;
	private Group node;

	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Group getNode() {
		return node;
	}
	
	public void setNode(Group node) {
		this.node = node;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
}
