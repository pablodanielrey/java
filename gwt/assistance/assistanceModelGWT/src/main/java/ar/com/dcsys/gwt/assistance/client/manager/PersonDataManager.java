package ar.com.dcsys.gwt.assistance.client.manager;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface PersonDataManager {

	public void setPin(String personId, String pin, Receiver<Boolean> rec);
	public void enroll(String personId, Receiver<String> rec);
	public void persist(Person p, Receiver<String> rec);
	public void transferFingerprints(String personId, Receiver<Boolean> rec);
	
}
