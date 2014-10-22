package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
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
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.SERVER),			
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailChangeSerializer", clazz="ar.com.dcsys.data.person.MailChange", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailChangeSerializer", clazz="ar.com.dcsys.data.person.MailChange", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailChangeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.MailChange>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.MailChangeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.MailChange>", type=SerializerType.SERVER)			
})
public interface MailChangesManager extends Manager {
	
	public void persist(MailChange mail, Person person, final Receiver<Void> receiver);
	public void remove(MailChange mail, final Receiver<Void> receiver);
	public void findAllBy(Person person, final Receiver<List<MailChange>> receiver);
	
	public void findMails(Person p, Receiver<List<Mail>> rec);
	public void addMail(String personId, Mail m, Receiver<Void> rec);
	public void removeMail(String personId, Mail m, Receiver<Void> rec);
	public void removeMails(String personId, List<Mail> mails, Receiver<Void> rec);
	public void addMails(String personId, List<Mail> mails, Receiver<Void> rec);	

}
