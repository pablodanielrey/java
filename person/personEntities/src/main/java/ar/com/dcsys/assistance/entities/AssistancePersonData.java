package ar.com.dcsys.assistance.entities;

import ar.com.dcsys.data.person.Person;

public interface AssistancePersonData {
	
	public Person getPerson();
	public void setPerson(Person person);

	public String getId();
	public void setId(String id);	
	
	public String getNotes();
	public void setNotes(String notes);
	
}
