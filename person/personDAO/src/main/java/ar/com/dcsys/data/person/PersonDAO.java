package ar.com.dcsys.data.person;

import java.util.List;

import ar.com.dcsys.exceptions.PersonException;

public interface PersonDAO {
	
	public List<String> findAllIds() throws PersonException;
	public String findIdByDni(String dni) throws PersonException;
	public List<String> findAllIdsBy(List<PersonType> type) throws PersonException;
	
	public List<Person> findAll() throws PersonException;
	public List<Person> findAll(Iterable<? extends String> ids) throws PersonException;

	public Person findById(String id) throws PersonException;
	public Person findByDni(String dni) throws PersonException;

	public List<PersonType> findAllTypes() throws PersonException;
	
	public void remove(Person p) throws PersonException;
	public String persist(Person p) throws PersonException;
	
	public void addMail(String personId, Mail mail) throws PersonException;
	public void removeMail(String personId, Mail mail) throws PersonException;
	public List<Mail> findAllMails(String personId) throws PersonException;
	
}
