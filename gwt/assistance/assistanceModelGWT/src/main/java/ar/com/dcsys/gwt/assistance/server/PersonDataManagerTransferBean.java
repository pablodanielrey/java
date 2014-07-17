package ar.com.dcsys.gwt.assistance.server;

import javax.inject.Inject;

import ar.com.dcsys.data.person.AssistancePersonData;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.assistance.shared.PersonDataManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.model.device.DevicesManager;
import ar.com.dcsys.model.person.AssistancePersonDataManager;

public class PersonDataManagerTransferBean implements PersonDataManagerTransfer {

	private final AssistancePersonDataManager personDataManager;
	private final DevicesManager devicesManager;
	
	@Inject
	public PersonDataManagerTransferBean(AssistancePersonDataManager personDataManager, DevicesManager devicesManager) {
		this.personDataManager = personDataManager;
		this.devicesManager = devicesManager;
	}
		
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPin(String personId, String pin, Receiver<Boolean> rec) {
		
		try {
			AssistancePersonData data = personDataManager.findById(personId);
			if (data == null) {
				data = new AssistancePersonData();
				data.setId(personId);
			}
			data.setPin(pin);
			personDataManager.persist(data);
			rec.onSuccess(true);
			
		} catch (Exception e) {
			rec.onError(e.getMessage());
		}
		
	}
	
	@Override
	public void enroll(String personId, Receiver<Boolean> rec) {
		try {
			devicesManager.enroll(personId);
			rec.onSuccess(true);
			
		} catch (PersonException | DeviceException e) {
			e.printStackTrace();
			rec.onError(e.getMessage());
		}
	}

}
