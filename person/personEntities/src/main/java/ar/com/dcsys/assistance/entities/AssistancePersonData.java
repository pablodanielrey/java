package ar.com.dcsys.assistance.entities;

import java.io.Serializable;

import ar.com.dcsys.data.person.Person;

public class AssistancePersonData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;	
	private String notes;
	private Person person;
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getId() {
		return id.toString();
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}

}
