package ar.com.dcsys.data.group;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;

public interface Group {
	
	public String getId();
	public void setId(String id);
	
	public String getName();
	public void setName(String name);
	
	public List<Mail> getMails();
	public void setMails(List<Mail> mails);
	
	public List<Person> getPersons();
	public void setPersons(List<Person> persons);
	
	public List<GroupType> getTypes();
	public void setTypes(List<GroupType> types);
	
}
