package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface MailChangesManager {
	
	public void persist(MailChange mail, Person person, final Receiver<Void> receiver);
	public void remove(MailChange mail, final Receiver<Void> receiver);
	public void findAllBy(Person person, final Receiver<List<MailChange>> receiver);
	
	public void findMails(Person p, Receiver<List<Mail>> rec);
	public void addMail(String personId, Mail m, Receiver<Void> rec);
	public void removeMail(String personId, Mail m, Receiver<Void> rec);
	public void removeMails(String personId, List<Mail> mails, Receiver<Void> rec);
	public void addMails(String personId, List<Mail> mails, Receiver<Void> rec);	

}
