package ar.com.dcsys.gwt.person.server;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.MailChangesManager;
import ar.com.dcsys.mail.MailException;
import ar.com.dcsys.mail.MailsManager;

public class MailChangesManagerBean implements MailChangesManager {

	private final MailsManager mailManager;
	private final ar.com.dcsys.model.PersonsManager personsManager;
	
	@Inject
	public MailChangesManagerBean(MailsManager mm, 
								  ar.com.dcsys.model.PersonsManager personsManager) {
		this.mailManager = mm;
		this.personsManager = personsManager;
	}
	
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(MailChange mail, Person person,	Receiver<Void> receiver) {
		try {
			mailManager.persist(person, mail);
			receiver.onSuccess(null);
		} catch (MailException e) {
			receiver.onError(e.getMessage());
		}
		
	}

	@Override
	public void remove(MailChange mail, Receiver<Void> receiver) {
		
		try {
			if (mail.getToken() == null) {
				mailManager.remove(mail.getMail().getMail());
				receiver.onSuccess(null);
			} else {
				mailManager.remove(mail);
				receiver.onSuccess(null);
			}
		} catch (MailException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findAllBy(Person person, Receiver<List<MailChange>> receiver) {

		try {
			// obtengo todos los cambios pendientes de mails
			List<MailChange> changes = mailManager.findAllMailChangeBy(person);
			
			// obtengo todos los mails confirmados.
			String personId = person.getId();
			List<Mail> mails = mailManager.findAllMails(personId);
			for (Mail m : mails) {
				MailChange mc = new MailChange();
				mc.setMail(m);
				mc.setPersonId(personId);
				mc.setConfirmed(true);
				changes.add(mc);
			}
			
		} catch (MailException e) {
			receiver.onError(e.getMessage());
		}
		
	}
	
	
	@Override
	public void findMails(Person p, Receiver<List<Mail>> rec) {
		try {
			List<Mail> mails = mailManager.findAllMails(p.getId());
			rec.onSuccess(mails);
		} catch (MailException e) {
			rec.onError(e.getMessage());
		}
	}
	
	@Override
	public void addMail(String personId, Mail m, Receiver<Void> rec) {
		try {
			mailManager.addMail(personId, m);
			rec.onSuccess(null);
		} catch (MailException e) {
			rec.onError(e.getMessage());
		}
	}
	
	@Override
	public void addMails(String personId, List<Mail> mails, Receiver<Void> rec) {
		try {
			for (Mail m : mails) {
				mailManager.addMail(personId, m);
			}
			rec.onSuccess(null);
		} catch (MailException e) {
			rec.onError(e.getMessage());
		}
	}
	
	@Override
	public void removeMail(String personId, Mail m, Receiver<Void> rec) {
		try {
			mailManager.removeMail(personId, m);
			rec.onSuccess(null);
		} catch (MailException e) {
			rec.onError(e.getMessage());
		}
	}
	
	@Override
	public void removeMails(String personId, List<Mail> mails,Receiver<Void> rec) {
		try {
			for (Mail m : mails) {
				mailManager.removeMail(personId,m);				
			}
			rec.onSuccess(null);
		} catch (MailException e) {
			rec.onError(e.getMessage());
		}
	}

}
