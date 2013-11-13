package ar.com.dcsys.data.auth.backend;

import java.util.List;

import ar.com.dcsys.data.auth.Permission;
import ar.com.dcsys.group.entities.Group;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;

public interface PermissionBackEnd {

		
	public void initialize() throws PersonException;
	public void destroy();
	
	public boolean hasPermissions(Person perons) throws PersonException;
	
	public List<Permission> findAll() throws PersonException;
	public List<Permission> findBy(Person person) throws PersonException;
	
	public interface Params {
		public Group findNodeById(String id) throws PersonException;
		public Person findPersonById(String id) throws PersonException;
	}
}
