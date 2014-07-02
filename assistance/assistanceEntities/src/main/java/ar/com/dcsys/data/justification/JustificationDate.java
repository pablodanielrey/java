package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.Date;

import ar.com.dcsys.data.person.Person;

public class JustificationDate  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
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
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
