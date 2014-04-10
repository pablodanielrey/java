package ar.com.dcsys.server;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.justification.JustificationDAO;
import ar.com.dcsys.data.log.AttLogDAO;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodDAO;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.AssistancePersonDataDAOParams;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

import javax.inject.Inject;

public class AssistanceParams implements PeriodDAO.Params, JustificationDAO.Params, AttLogDAO.Params, AssistancePersonDataDAOParams {

	private final PersonsManager personsManager; 
	
	@Inject
	public AssistanceParams(PersonsManager personsManager) {
		this.personsManager = personsManager;
	}

	@Override
	public Person findPersonById(String person_id) throws PersonException {
		return personsManager.findById(person_id);
	}

	@Override
	public Device findDeviceById(String device_id) throws DeviceException {
		// TODO Auto-generated method stub
		return null;
	}
}
