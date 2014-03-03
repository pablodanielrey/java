package ar.com.dcsys.model;

import java.util.List;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public interface MailChangesManager {

	public void persist(Person person, MailChange change) throws PersonException;
	public void remove(MailChange mail) throws PersonException;
	public void remove(String mail) throws PersonException;
	public List<MailChange> findAllBy(Person person) throws PersonException;
	public void processByToken(String token) throws PersonException;
	
	
}
