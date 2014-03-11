package ar.com.dcsys.data.period;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;

public class PeriodAssignationBean  implements Serializable, PeriodAssignation {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private Date start;
	private Person person;
	private PeriodType type;
	
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
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
