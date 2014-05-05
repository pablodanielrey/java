package ar.com.dcsys.server.person;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.data.auth.principals.UserNamePrincipal;
import ar.com.dcsys.data.group.GroupLdapDAOParams;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AuthenticationException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.auth.AuthManager;

public class GroupParams implements GroupLdapDAOParams {

	private final PersonsManager personsManager;
	private final AuthManager authManager;
	
	@Inject
	public GroupParams(AuthManager authManager, PersonsManager personsManager) {
		this.personsManager = personsManager;
		this.authManager = authManager;
	}
	
	
	@Override
	public List<Person> findPersonByMemberField(String uid) throws PersonException {
		if (uid == null) {
			throw new PersonException("uid == null");
		}
		
		Person p = personsManager.findByDni(uid);
		if (p != null) {
			return Arrays.asList(p);
		}
		
		UserNamePrincipal userName = new UserNamePrincipal(uid);
		try {
			
			String id = authManager.findIdByPrincipal(userName);
			Person person = personsManager.findById(id);
			return Arrays.asList(person);
			
		} catch (AuthenticationException e) {
			throw new PersonException(e);
		}
		
	}
	
	@Override
	public String getMemberFieldContent(Person p) throws PersonException {
		String id = p.getId();
		IdPrincipal principal = new IdPrincipal(id);
		try {
			List<Principal> principals = authManager.findAllPrincipals(principal);
			for (Principal pi : principals) {
				if (pi instanceof UserNamePrincipal) {
					return ((UserNamePrincipal)pi).getName();
				}
			}
			throw new PersonException("Nombre de usuario no encontrado");
			
		} catch (AuthenticationException e) {
			throw new PersonException(e);
		}
	};
	
}
