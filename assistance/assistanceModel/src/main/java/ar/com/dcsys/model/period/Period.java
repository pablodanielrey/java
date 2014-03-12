package ar.com.dcsys.model.period;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;

public class Period implements Serializable {

	private static final long serialVersionUID = 1L;

	private Person person;
	private Date start;
	private Date end;
	private List<WorkedHours> whs = new ArrayList<WorkedHours>();
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = resetMiliseconds(start);
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = resetMiliseconds(end);
	}

	public List<WorkedHours> getWorkedHours() {
		return whs;
	}
	
	public void setWorkedHours(List<WorkedHours> whs) {
		this.whs = Collections.unmodifiableList(whs);
	}

	private Date resetMiliseconds(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
}
