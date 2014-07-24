package ar.com.dcsys.gwt.assistance.server;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import ar.com.dcsys.data.person.AssistancePersonData;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.assistance.shared.PersonDataManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.messages.shared.TransportEvent;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.model.device.DevicesManager;
import ar.com.dcsys.model.device.EnrollAction;
import ar.com.dcsys.model.device.EnrollManager;
import ar.com.dcsys.model.person.AssistancePersonDataManager;
import ar.com.dcsys.security.Fingerprint;

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
	
	@Inject
	private Event<TransportEvent> transportEvent;
	
	@Override
	public void enroll(String personId, final Receiver<String> rec) {
		try {
			EnrollManager em = new EnrollManager() {
				@Override
				public void onMessage(EnrollAction action) {
					
					TransportEvent tr = new TransportEvent();
					tr.setMessage(action.toString());
					tr.setTransportReceiver(new TransportReceiver() {
						@Override
						public void onSuccess(String ok) {
						}
						@Override
						public void onFailure(String error) {
						}
					});
					tr.setType("enroll");
					transportEvent.fire(tr);
				}
				
				@Override
				public void onSuccess(Fingerprint fingerprint) {
					rec.onSuccess(fingerprint.getId());					
				}
			};
			
			devicesManager.enroll(personId, em);
		
		} catch (PersonException | DeviceException e) {
			e.printStackTrace();
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void persist(Person person, Receiver<String> rec) {
		
		try {
			devicesManager.persist(person);
			rec.onSuccess(person.getId());
			
		} catch (PersonException | DeviceException e) {
			e.printStackTrace();
			rec.onError(e.getMessage());
		}

		
	}
	
}
