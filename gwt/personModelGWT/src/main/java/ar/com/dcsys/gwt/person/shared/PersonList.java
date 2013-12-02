package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import ar.com.dcsys.data.person.Person;

/**
 * Clase interna que se usa para serializar por autobeans una lista de personas.
 * 
 * @author pablo
 *
 */
public interface PersonList {

	public void setPersons(List<Person> list);
	public List<Person> getPersons();
	
}
