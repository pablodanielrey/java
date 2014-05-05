package ar.com.dcsys.server.assistance.params;

import javax.inject.Inject;

import ar.com.dcsys.data.period.PeriodDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

public class PeriodDAOParams implements PeriodDAO.Params {

	private final PersonsManager personsManager;
	
	@Inject
	public PeriodDAOParams(PersonsManager personsManager) {
		this.personsManager = personsManager;
	}
	
	@Override
	public Person findPersonById(String id) throws PersonException {
		return personsManager.findById(id);
	}

}
