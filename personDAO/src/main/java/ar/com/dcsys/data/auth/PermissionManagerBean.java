package ar.com.dcsys.data.auth;

import java.util.List;

import ar.com.dcsys.data.auth.backend.PermissionBackEnd;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;

public class PermissionManagerBean implements PermissionManager {

	
	private final PermissionBackEnd backend;
	
	public PermissionManagerBean(PermissionBackEnd backEnd) {
		this.backend = backEnd;
	}
	
	@Override
	public List<Permission> findAll() throws PersonException {
		return backend.findAll();
	}
	
	@Override
	public List<Permission> findBy(Person person) throws PersonException {
		return backend.findBy(person);
	}
	
	@Override
	public boolean hasPermissions(Person person) throws PersonException {
		return backend.hasPermissions(person);
	}
	
}
