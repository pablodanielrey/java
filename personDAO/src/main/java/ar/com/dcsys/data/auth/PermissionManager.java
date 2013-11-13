package ar.com.dcsys.data.auth;

import java.util.List;

import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;

public interface PermissionManager {

	public boolean hasPermissions(Person person) throws PersonException;
	public List<Permission> findAll() throws PersonException;
	public List<Permission> findBy(Person person) throws PersonException;	
	
}
