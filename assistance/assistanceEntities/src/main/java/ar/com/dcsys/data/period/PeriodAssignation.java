package ar.com.dcsys.data.period;

import java.util.Date;

import ar.com.dcsys.data.person.Person;

public interface PeriodAssignation {

	public String getId();
	public void setId(String id);
	
	public Date getStart();
	public void setStart(Date pstart);
	
	public Person getPerson();
	public void setPerson(Person person);
	
	public PeriodType getType();
	public void setType(PeriodType type);
	
}
