package ar.com.dcsys.model;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.mail.MailChangeDAO;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

@Singleton
public class MailChangesManagerBean implements MailChangesManager {

	private final MailChangeDAO mailChangeDAO;
	private final PersonsManager personsManager;
	
	@Inject
	public MailChangesManagerBean(MailChangeDAO mailChangeDAO, PersonsManager personsManager) {
		this.mailChangeDAO = mailChangeDAO;
		this.personsManager = personsManager;
	}
	
	@Override
	public void persist(Person person, MailChange mailChange) throws PersonException {
		
		if (mailChange.getMail() == null || mailChange.getMail().getMail() == null) {
			throw new PersonException("mail == null");
		}
		
		String personId = person.getId();
		if (personId == null) {
			throw new PersonException("person.id == null");
		}
		
		String mPersonId = mailChange.getPersonId();
		if (mPersonId != null && (!personId.equals(mPersonId))) {
			throw new PersonException("El id de la persona pasada como parámetro debe ser igual al id de la persona configurada en el cambio de mail\n" + personId + " != " + mPersonId);
		}
		
		mailChange.setPersonId(personId);		

		String uuid = UUID.randomUUID().toString();
		mailChange.setToken(uuid);
		mailChange.setConfirmed(false);
		
		mailChangeDAO.persist(mailChange);
	}
	
	
	@Override
	public void remove(MailChange mail) throws PersonException {
		mailChangeDAO.remove(mail);	
	}
	
	@Override
	public void remove(String mail) throws PersonException {
		List<MailChange> changes = mailChangeDAO.findByMail(mail);
		for (MailChange change : changes) {
			mailChangeDAO.remove(change);
		}
	}
	
	@Override
	public List<MailChange> findAllBy(Person person) throws PersonException {
		return mailChangeDAO.findAllBy(person.getId());
	}

	
	@Override
	public void processByToken(String token) throws PersonException {
		MailChange mc = mailChangeDAO.findByToken(token);
		if (mc.isConfirmed()) {
			return;
		}
		
		Mail mail = mc.getMail();
		String m2 = mail.getMail().trim().toLowerCase();
		
		String personId = mc.getPersonId();
		
		boolean found = false;
		List<Mail> mails = personsManager.findAllMails(personId);
		for (Mail m : mails) {
			String m1 = m.getMail().trim().toLowerCase();
			if (m2.equals(m1)) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			personsManager.addMail(personId, mail);
		}
		
		mailChangeDAO.remove(mc);
	}
	
}
