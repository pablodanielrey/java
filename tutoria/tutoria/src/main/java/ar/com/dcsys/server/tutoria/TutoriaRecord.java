package ar.com.dcsys.server.tutoria;

import java.io.Serializable;
import java.util.Date;

import ar.com.dcsys.data.person.Person;

public class TutoriaRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private Person person;
	private String type;
	private Date logDate;
	private Person owner;
	
	public TutoriaRecord(Date date, Person person, String type, Date logDate, Person owner) {
		this.date = date;
		this.person = person;
		this.type = type;
		this.logDate = logDate;
		this.owner = owner;
	}

	
	public Date getLogDate() {
		return logDate;
	}


	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}


	public Person getOwner() {
		return owner;
	}


	public void setOwner(Person owner) {
		this.owner = owner;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
