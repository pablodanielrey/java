package ar.com.dcsys.server.person;

import javax.inject.Inject;

import ar.com.dcsys.data.person.AssistancePersonDataDAOParams;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

public class AssistanceParams implements AssistancePersonDataDAOParams {

	private final PersonsManager personsManager;
	
	@Inject
	public AssistanceParams(PersonsManager personsManager) {
		this.personsManager = personsManager;
	}
	
	@Override
	public Person findPersonById(String id) throws PersonException {
		return personsManager.findById(id);
	}
	
}
