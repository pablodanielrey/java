package ar.com.dcsys.model.person;

import java.util.List;

import ar.com.dcsys.data.person.AssistancePersonData;
import ar.com.dcsys.exceptions.PersonDataException;
import ar.com.dcsys.exceptions.PersonException;

public interface AssistancePersonDataManager {

	public List<AssistancePersonData> findAll() throws PersonDataException, PersonException;
	public AssistancePersonData findById(String id) throws PersonDataException, PersonException;
	
	public String persist(AssistancePersonData data) throws PersonDataException;	
	
}
