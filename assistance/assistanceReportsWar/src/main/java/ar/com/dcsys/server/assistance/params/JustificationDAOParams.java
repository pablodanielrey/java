package ar.com.dcsys.server.assistance.params;

import javax.inject.Inject;

import ar.com.dcsys.data.justification.JustificationDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

public class JustificationDAOParams implements JustificationDAO.Params {

	private final PersonsManager personsManager;
	
	@Inject
	public JustificationDAOParams(PersonsManager personsManager) {
		this.personsManager = personsManager;
	}
	

	@Override
	public Person findPersonById(String id) throws PersonException {
		return personsManager.findById(id);
	}

}
