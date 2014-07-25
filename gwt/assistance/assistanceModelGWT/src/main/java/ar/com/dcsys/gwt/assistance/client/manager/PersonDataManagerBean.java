package ar.com.dcsys.gwt.assistance.client.manager;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.shared.PersonDataManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.client.GWT;

public class PersonDataManagerBean implements PersonDataManager {

	private final WebSocket socket;
	private final PersonDataManagerTransfer personDataManagerTransfer = GWT.create(PersonDataManagerTransfer.class);
	
	@Inject
	public PersonDataManagerBean(WebSocket ws) {
		this.socket = ws;
		this.personDataManagerTransfer.setTransport(ws);
	}
	
	@Override
	public void setPin(String personId, String pin, Receiver<Boolean> rec) {
		personDataManagerTransfer.setPin(personId, pin, rec);
	}

	@Override
	public void enroll(String personId, Receiver<String> rec) {
		personDataManagerTransfer.enroll(personId, rec);
	}
	
	@Override
	public void persist(Person p, Receiver<String> rec) {
		personDataManagerTransfer.persist(p, rec);
	}
	
	@Override
	public void transferFingerprints(String personId, Receiver<Boolean> rec) {
		personDataManagerTransfer.transferFingerprints(personId, rec);
	}
	
}
