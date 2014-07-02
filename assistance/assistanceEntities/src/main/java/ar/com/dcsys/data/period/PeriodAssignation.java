package ar.com.dcsys.data.period;

import java.io.Serializable;
import java.util.Date;

import ar.com.dcsys.data.person.Person;

public class PeriodAssignation  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private Date start;
	private Person person;
	private PeriodType type;
	
	public String getId() {		
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}
	
	public void setStart(Date pstart) {
		this.start = pstart;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public PeriodType getType() {
		return type;
	}
	
	public void setType(PeriodType type) {
		this.type = type;
	}

}
