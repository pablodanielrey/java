package ar.com.dcsys.data.period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;

public class Period {

	private Person person;
	private Date start;
	private Date end;
	private List<? extends WorkedHours> whs = new ArrayList<>();
	
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
		//this.start = resetMiliseconds(start);
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		//this.end = resetMiliseconds(end);
		this.end = end;
	}

	public List<? extends WorkedHours> getWorkedHours() {
		return whs;
	}
	
	public void setWorkedHours(List<? extends WorkedHours> whs) {
		this.whs = Collections.unmodifiableList(whs);
	}

	/*
	private Date resetMiliseconds(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	*/
	
}
