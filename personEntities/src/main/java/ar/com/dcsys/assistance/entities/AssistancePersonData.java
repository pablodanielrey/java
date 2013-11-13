package ar.com.dcsys.assistance.entities;

import java.io.Serializable;
import java.util.UUID;

import ar.com.dcsys.person.entities.Person;

public class AssistancePersonData implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;
	private Long version = 1l;	
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
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	
	
}
