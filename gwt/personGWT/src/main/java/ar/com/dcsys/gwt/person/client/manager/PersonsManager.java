package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.gwt.person.shared.PersonProxy;

public interface PersonsManager {

	/**
	 * Envía el mensaje de persist al servidor.
	 * @param person, persona a crear o actualizar
	 * @param receiver, receptor de la respuesta.
	 */
	public void persist(PersonProxy person, Receiver<String> receiver);
	
	/**
	 * Busca en el servidor todas las personas. 
	 * @param receiver
	 */
	public void findAll(Receiver<List<PersonProxy>> receiver);
	
}
