package ar.com.dcsys.gwt.person.shared.manager;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface PersonsManager extends Manager {

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
	
	
	/**
	 * Genera el reporte con todas las personas de la base.
	 * @param rec
	 */
	public void report(Receiver<Document> rec);
	
	public void findAllReports(Receiver<List<Document>> rec);
}
