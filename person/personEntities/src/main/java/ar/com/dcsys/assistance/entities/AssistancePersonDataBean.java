package ar.com.dcsys.assistance.entities;

import java.io.Serializable;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;

public class AssistancePersonDataBean implements Serializable, AssistancePersonData {
	private static final long serialVersionUID = 1L;

	private UUID id;	
	private String notes;
	private Person person;
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}	
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}

}
