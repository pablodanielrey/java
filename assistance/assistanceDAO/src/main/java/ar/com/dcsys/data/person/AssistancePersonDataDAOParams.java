package ar.com.dcsys.data.person;

import ar.com.dcsys.exceptions.PersonException;

public interface AssistancePersonDataDAOParams {

	public Person findPersonById(String person_id) throws PersonException;
	
}
