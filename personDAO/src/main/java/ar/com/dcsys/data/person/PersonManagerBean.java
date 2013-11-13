package ar.com.dcsys.data.person;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.backend.PersonBackEnd;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;
import ar.com.dcsys.person.entities.types.PersonType;

public class PersonManagerBean implements PersonManager {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(PersonManagerBean.class.getName());
	
	private final PersonBackEnd backEnd;
	
	public PersonManagerBean(PersonBackEnd backend) {
		backEnd = backend;
	}
	
	@Override
	public Person findByStudentNumber(String studentNumber)	throws PersonException {
		return backEnd.findByStudentNumber(studentNumber);
	}
	
	@Override
	public List<Person> findAll() throws PersonException {
		return backEnd.findAll();
	}
	
	@Override
	public List<String> findAllIds() throws PersonException {
		return backEnd.findAllIds();
	}
	
	@Override
	public String findIdByDni(String dni) throws PersonException {
		return backEnd.findIdByDni(dni);
	}
	
	@Override
	public List<Person> findAll(Iterable<? extends String> ids) throws PersonException {
		return backEnd.findAll(ids);
	}
	

	@Override
	public List<String> findAllIdsBy(List<PersonType> types) throws PersonException {
		if (types == null) {
			throw new PersonException("no se puede buscar por tipos = null");
		}
		return backEnd.findAllIdsBy(types);
	}
	
	@Override
	public Person findById(String id) throws PersonException {
		return backEnd.findById(id);
	}

	@Override
	public Person findByDni(String dni) throws PersonException {
		return backEnd.findByDni(dni);
	}
	
	@Override
	public Person findByUid(String uid) throws PersonException {
		return backEnd.findByUid(uid);
	}
	
	@Override
	public String persist(Person p) throws PersonException {
		return backEnd.persist(p);
	}

	@Override
	public void remove(Person p) throws PersonException {
	}

	@Override
	public List<PersonType> findAllTypes() throws PersonException {
		return backEnd.findAllTypes();
	}
	
}
