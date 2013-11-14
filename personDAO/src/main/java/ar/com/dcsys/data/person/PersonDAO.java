package ar.com.dcsys.data.person;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;
import ar.com.dcsys.person.entities.types.PersonType;

public interface PersonDAO extends Serializable {
	
	public Person findByStudentNumber(String number) throws PersonException;

	public List<String> findAllIds() throws PersonException;
	public String findIdByDni(String dni) throws PersonException;
	public List<String> findAllIdsBy(List<PersonType> type) throws PersonException;
	
	public List<Person> findAll() throws PersonException;
	public List<Person> findAll(Iterable<? extends String> ids) throws PersonException;

	public Person findById(String id) throws PersonException;
	public Person findByDni(String dni) throws PersonException;
	public Person findByUid(String uid) throws PersonException;
	public List<PersonType> findAllTypes() throws PersonException;
	
	public void remove(Person p) throws PersonException;
	public String persist(Person p) throws PersonException;

	public void initialize() throws PersonException;
	public void destroy() throws PersonException; 
	
}
