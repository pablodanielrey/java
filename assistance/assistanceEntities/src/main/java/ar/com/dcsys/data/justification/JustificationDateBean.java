package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;

public class JustificationDateBean  implements Serializable, JustificationDate {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private Person person;
	private Justification justification;
	private Date start;
	private Date end;
	private String notes;
	
	
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Justification getJustification() {
		return justification;
	}

	public void setJustification(Justification justification) {
		this.justification = justification;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	

}
