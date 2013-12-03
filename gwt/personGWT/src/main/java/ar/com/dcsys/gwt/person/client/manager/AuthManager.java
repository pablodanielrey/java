package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.Person;

public interface AuthManager {

	public void isUserInRole(String role, Receiver<Boolean> rec);
	public void findByPerson(Person p, Receiver<DCSysPrincipal> rec);
	public void findAllCredentials(DCSysPrincipal p, Receiver<List<DCSysCredentials>> rec);
	
	
}
