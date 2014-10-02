package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers = {
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.PersonType>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.PersonType>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.SERVER)		
})
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
	public void findAll(List<PersonType> types, Receiver<List<Person>> rec);
	
	
	public void findMails(Person p, Receiver<List<Mail>> rec);
	public void addMail(Person p, Mail m, Receiver<Void> rec);
	
	
	public void findByDni(String dni, Receiver<Person> rec);								// PersonDAtaActvity
	public void findById(String id, Receiver<Person> rec);
	
	
	/**
	 * Genera el reporte con todas las personas de la base.
	 * @param rec
	 */
	public void report(Receiver<Document> rec);
	
	public void findAllReports(Receiver<List<Document>> rec);
}
