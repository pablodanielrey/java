package ar.com.dcsys.data.group;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public interface GroupLdapDAOParams {

	public List<Person> findPersonByMemberField(String uid) throws PersonException;
	public String getMemberFieldContent(Person p) throws PersonException;
	
}
