package ar.com.dcsys.server.assistance.params;

import javax.inject.Inject;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.device.DeviceDAO;
import ar.com.dcsys.data.log.AttLogDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

public class AttLogDAOParams implements AttLogDAO.Params {

	private final PersonsManager personsManager;
	
	//TODO: hay que cambiar por un manager cuando se cree asi es la capa del modelo. que posiblemente tenga cache.
	private final DeviceDAO devicesManager;				
	
	@Inject
	public AttLogDAOParams(PersonsManager personsManager, DeviceDAO devicesManager) {
		this.personsManager = personsManager;
		this.devicesManager = devicesManager;
	}
	
	@Override
	public Person findPersonById(String person_id) throws PersonException {
		return personsManager.findById(person_id);
	}

	@Override
	public Device findDeviceById(String device_id) throws DeviceException {
		return devicesManager.findById(device_id);
	}

}
