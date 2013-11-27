package ar.com.dcsys.gwt.person.client.manager;

import ar.com.dcsys.gwt.person.shared.PersonProxy;

public interface PersonsManager {

	public PersonProxy getPerson();
	public void persist(PersonProxy person, Receiver<String> receiver);
	
}
