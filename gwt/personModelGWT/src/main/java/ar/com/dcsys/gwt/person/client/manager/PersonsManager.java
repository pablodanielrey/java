package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public interface PersonsManager {

	public void getLoggedPerson(Receiver<Person> rec);
	
	/**
	 * Envía el mensaje de persist al servidor.
	 * @param person, persona a crear o actualizar
	 * @param receiver, receptor de la respuesta.
	 */
	public void persist(Person person, Receiver<String> receiver);
	
	/**
	 * Busca en el servidor todas las personas. 
	 * @param receiver
	 */
	public void findAll(Receiver<List<Person>> receiver);
	
	public void findAllPersonValue(Receiver<List<PersonValueProxy>> rec);
	public void findAllPersonValue(List<PersonType> types, Receiver<List<PersonValueProxy>> rec);
	
	
	public void findMails(Person p, Receiver<List<Mail>> rec);
	public void addMail(Person p, Mail m, Receiver<Void> rec);
	
	
	public void findAllTypes(Receiver<List<PersonType>> rec);								// PersonDataActivity
	public void findByDni(String dni, Receiver<Person> rec);								// PersonDAtaActvity
	public void findById(String id, Receiver<Person> rec);
	
	public void findAssistancePersonData(Person p, Receiver<AssistancePersonData> rec);		// AssistancePersonDataActivity
	
}
