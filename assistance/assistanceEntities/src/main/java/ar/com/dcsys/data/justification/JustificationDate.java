package ar.com.dcsys.data.justification;

import java.util.Date;

import ar.com.dcsys.data.person.Person;

public interface JustificationDate {
	
	public String getNotes();
	public void setNotes(String notes);
	
	public String getId();
	public void setId(String id);

	public Person getPerson();
	public void setPerson(Person person);

	public Justification getJustification();
	public void setJustification(Justification justification);

	public Date getStart();
	public void setStart(Date start);
	
	public Date getEnd();
	public void setEnd(Date end);
	
}
