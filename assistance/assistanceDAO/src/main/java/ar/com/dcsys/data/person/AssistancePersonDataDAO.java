package ar.com.dcsys.data.person;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.PersonDataException;
import ar.com.dcsys.exceptions.PersonException;

public interface AssistancePersonDataDAO extends Serializable {

	public List<AssistancePersonData> findAll() throws PersonDataException, PersonException;
	public AssistancePersonData findById(String id) throws PersonDataException, PersonException;
	
	public String persist(AssistancePersonData data) throws PersonDataException;
	
	
}
