package ar.com.dcsys.model.person;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.AssistancePersonData;
import ar.com.dcsys.data.person.AssistancePersonDataDAO;
import ar.com.dcsys.exceptions.PersonDataException;
import ar.com.dcsys.exceptions.PersonException;

public class AssistancePersonDataManagerBean implements AssistancePersonDataManager {

	private final AssistancePersonDataDAO assistancePersonDataDAO;
	
	@Inject
	public AssistancePersonDataManagerBean(AssistancePersonDataDAO assistancePersonDataDAO) {
		this.assistancePersonDataDAO = assistancePersonDataDAO;
	}
	
	
	@Override
	public List<AssistancePersonData> findAll() throws PersonDataException, PersonException {
		return assistancePersonDataDAO.findAll();
	}

	@Override
	public AssistancePersonData findById(String id) throws PersonDataException, PersonException {
		return assistancePersonDataDAO.findById(id);
	}

	@Override
	public String persist(AssistancePersonData data) throws PersonDataException {
		return assistancePersonDataDAO.persist(data);
	}

	
	
}
