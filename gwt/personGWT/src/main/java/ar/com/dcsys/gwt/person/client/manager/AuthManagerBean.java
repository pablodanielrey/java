package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public class AuthManagerBean implements AuthManager {

	@Override
	public void isUserInRole(String role, Receiver<Boolean> rec) {
		rec.onSuccess(true);
	}
	
	@Override
	public void findAllCredentials(DCSysPrincipal p, Receiver<List<DCSysCredentials>> rec) {
		rec.onSuccess(null);
	}
	
	@Override
	public void findByPerson(Person p, Receiver<DCSysPrincipal> rec) {
		rec.onSuccess(null);
	}
	
	
	
}
