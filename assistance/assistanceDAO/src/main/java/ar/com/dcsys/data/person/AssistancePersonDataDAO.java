package ar.com.dcsys.data.person;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.assistance.AssistancePersonDataException;
import ar.com.dcsys.assistance.entities.AssistancePersonData;
import ar.com.dcsys.exceptions.PersonException;

public interface AssistancePersonDataDAO extends Serializable {

	public List<AssistancePersonData> findAll() throws AssistancePersonDataException, PersonException;
	
	public String persist(AssistancePersonData data) throws AssistancePersonDataException;
	
	public AssistancePersonData findById(String id) throws AssistancePersonDataException, PersonException;
	public AssistancePersonData findBy(Person person) throws AssistancePersonDataException, PersonException;
	
}
