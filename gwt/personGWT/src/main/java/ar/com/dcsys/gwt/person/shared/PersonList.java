package ar.com.dcsys.gwt.person.shared;

import java.util.List;

/**
 * Clase interna que se usa para serializar por autobeans una lista de personas.
 * 
 * @author pablo
 *
 */
public interface PersonList {

	public void setPersons(List<PersonProxy> list);
	public List<PersonProxy> getPersons();
	
}
