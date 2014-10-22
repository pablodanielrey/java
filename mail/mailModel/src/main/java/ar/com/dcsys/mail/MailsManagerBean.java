package ar.com.dcsys.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import ar.com.dcsys.config.Config;
import ar.com.dcsys.data.mail.MailChangeDAO;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.exceptions.PersonException;

@Singleton
public class MailsManagerBean implements MailsManager {

	
	private static final Logger logger = Logger.getLogger(MailsManagerBean.class.getName());
	
	private final MailChangeDAO mailChangeDAO;
	private final PersonDAO personDAO;
		
	@Inject @Config String user;
	@Inject @Config String password;
	@Inject @Config String server;
	@Inject @Config String auth;			// true o false
	
	
	@Inject
	public MailsManagerBean(MailChangeDAO mailChangeDAO, PersonDAO personDAO) {
		this.mailChangeDAO = mailChangeDAO;
		this.personDAO = personDAO;
	}
	
	/**
	 * -----------------------------------------------------------------
	 * --------------------- ENVIO DE MAILS ----------------------------
	 * -----------------------------------------------------------------
	 */
	private Session getSession() {

		Properties env = new Properties();
		env.put("mail.smtp.host", server);
		env.put("mail.debug",true);
		
		if (auth == null) {
			if (user != null && password != null) {
				env.put("mail.smtp.auth",true);
			} else {
				env.put("mail.smtp.auth",false);
			}
		} else {
			env.put("mail.smtp.auth",auth);
		}
	
		return Session.getInstance(env,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user,password);
			}
		});
	}	
	
	
	@Override
	public void sendMail(String from, String to, String subject, String content) throws MailException {
	
		if (subject == null || from == null || to == null || content == null) {
			throw new MailException("subject == null || from == null || to == null || content == null");
		}
		 
		try {
			
			Session mailSession = getSession();
			
			InternetAddress aFrom = new InternetAddress(from);
			InternetAddress aTo = new InternetAddress(to);
			
			BodyPart bp = new MimeBodyPart();
			bp.setText(content);
			
			MimeMultipart mm = new MimeMultipart();
			mm.addBodyPart(bp);
			
			Message message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setFrom(aFrom);
			message.setRecipient(RecipientType.TO, aTo);
			message.setContent(mm);
			
			Transport.send(message);
			
		 } catch (MessagingException e) {
			 logger.log(Level.SEVERE,"Error enviando\nfrom: " + from + " subject :  " + subject + " to : " + to + " causa : " + e.getMessage());
			 throw new MailException(e); 
		 }
	}
	
	/**
	 * -------------------------------------------------------------------------------
	 * -------------------------- MAIL CHANGE ----------------------------------------
	 * -------------------------------------------------------------------------------
	 */
	
	@Override
	public void persist(Person person, MailChange mailChange) throws MailException {
		
		if (mailChange.getMail() == null || mailChange.getMail().getMail() == null) {
			throw new MailException("mail == null");
		}
		
		String personId = person.getId();
		if (personId == null) {
			throw new MailException("person.id == null");
		}
		
		String mPersonId = mailChange.getPersonId();
		if (mPersonId != null && (!personId.equals(mPersonId))) {
			throw new MailException("El id de la persona pasada como parámetro debe ser igual al id de la persona configurada en el cambio de mail\n" + personId + " != " + mPersonId);
		}
		
		mailChange.setPersonId(personId);		

		String uuid = UUID.randomUUID().toString();
		mailChange.setToken(uuid);
		mailChange.setConfirmed(false);
		
		try {
			mailChangeDAO.persist(mailChange);
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public void remove(MailChange mail) throws MailException {
		try {
			mailChangeDAO.remove(mail);
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public List<MailChange> findAllMailChangeBy(Person person) throws MailException {
		try {
			return mailChangeDAO.findAllBy(person.getId());
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public void processByToken(String token) throws MailException {
		try {
			MailChange mc = mailChangeDAO.findByToken(token);
			if (mc.isConfirmed()) {
				return;
			}
			
			Mail mail = mc.getMail();
			String m2 = mail.getMail().trim().toLowerCase();
			
			String personId = mc.getPersonId();
			
			boolean found = false;
			List<Mail> mails = findAllMails(personId);
			for (Mail m : mails) {
				String m1 = m.getMail().trim().toLowerCase();
				if (m2.equals(m1)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				addMail(personId, mail);
			}
			
			mailChangeDAO.remove(mc);
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public void remove(String mail) throws MailException {
		try {
			List<MailChange> changes = mailChangeDAO.findByMail(mail);
			for (MailChange change : changes) {
				mailChangeDAO.remove(change);
			}
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	/**
	 * -----------------------------------------------------------------------------
	 * ----------------------- MAIL CONFIRMADOS ------------------------------------
	 * -----------------------------------------------------------------------------
	 */
	private boolean include(String personId, Mail mail) throws MailException {		
		String mailStr = mail.getMail().trim();
		List<Mail> mails = findAllMails(personId);
		for (Mail m : mails) {
			if (m.getMail().equals(mailStr)) {
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public void addMail(String personId, Mail mail) throws MailException {
		try {
			if (mail == null || mail.getMail() == null) {
				return;
			}
			if (!include(personId,mail)) { 
				personDAO.addMail(personId, mail);
			}
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public List<Mail> findAllMails(String personId) throws MailException {
		try {
			return personDAO.findAllMails(personId);
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
	@Override
	public void removeMail(String personId, Mail mail) throws MailException {
		try {
			personDAO.removeMail(personId, mail);
		} catch (PersonException e) {
			throw new MailException(e);
		}
	}
	
}
