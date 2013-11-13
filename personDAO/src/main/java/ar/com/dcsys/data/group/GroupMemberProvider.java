package ar.com.dcsys.data.group;

import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;

/**
 * @author pablo
 *
 * Interfaz necesaria para resolver el tema del campo miembro del grupo cuando 
 * se manejan grupos de tipo posix dentro del ldap.
 * puede ser el dn del objeto integrante o el uid.
 *
 */
public interface GroupMemberProvider {

	public String getMemberField(Person person) throws PersonException;
	public Person getPerson(String memberField) throws PersonException;
	
}
