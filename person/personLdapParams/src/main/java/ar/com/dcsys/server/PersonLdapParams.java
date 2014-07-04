package ar.com.dcsys.server;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.auth.principals.DniPrincipal;
import ar.com.dcsys.data.auth.principals.UserNamePrincipal;
import ar.com.dcsys.data.group.GroupLdapDAOParams;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;


public class PersonLdapParams implements GroupLdapDAOParams {

	private final PersonsManager personsManager;
	
	@Inject
	public PersonLdapParams(PersonsManager personsManager) {
		this.personsManager = personsManager;
	}
	
	@Override
	public List<Person> findPersonByMemberField(String uid) throws PersonException {
		Person person = personsManager.findByPrincipal(new UserNamePrincipal(uid));
		return Arrays.asList(person);
	}

	@Override
	public String getMemberFieldContent(Person p) throws PersonException {
		List<Principal> principals = personsManager.getPrincipals(p);
		for (Principal pi : principals) {
			if (pi instanceof DniPrincipal) {
				return pi.getName();
			}
		}
		throw new PersonException("DniPrincipal for person not found");
	}


}
