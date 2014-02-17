package ar.com.dcsys.model;

import java.security.Principal;
import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.exceptions.PersonException;

public interface PersonsManager {

	public Person findByPrincipal(Principal principal) throws PersonException;
	public List<Principal> getPrincipals(Person person) throws PersonException;
	
	public Person getLoggedPerson() throws PersonException;
	
	public List<Person> findAll() throws PersonException;
	public List<Person> findAllBy(List<PersonType> types) throws PersonException;
	
	public Person findById(String id) throws PersonException;
	public Person findByDni(String dni) throws PersonException;
	
	public String persist(Person p) throws PersonException;
	public void remove(Person p) throws PersonException;	

	public List<PersonType> findAllTypes() throws PersonException;
	
	
	void addMail(String personId, Mail mail) throws PersonException;
	void removeMail(String personId, Mail mail) throws PersonException;
	List<Mail> findAllMails(String personId) throws PersonException;
	
}
