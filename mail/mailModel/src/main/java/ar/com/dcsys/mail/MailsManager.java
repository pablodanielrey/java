package ar.com.dcsys.mail;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;



public interface MailsManager {

	public void sendMail(String from, String to, String subject, String content) throws MailException;
	
	public void persist(Person person, MailChange change) throws MailException;
	public void remove(MailChange mail) throws MailException;
	public List<MailChange> findAllMailChangeBy(Person person) throws MailException;
	public void processByToken(String token) throws MailException;	
	

	public void remove(String mail) throws MailException;
	
	public void addMail(String personId, Mail mail) throws MailException;
	public void removeMail(String personId, Mail mail) throws MailException;
	public List<Mail> findAllMails(String personId) throws MailException;
}
