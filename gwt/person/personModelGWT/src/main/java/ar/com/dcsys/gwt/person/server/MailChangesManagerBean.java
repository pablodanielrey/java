package ar.com.dcsys.gwt.person.server;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.MailChangesManager;

public class MailChangesManagerBean implements MailChangesManager {

	private final ar.com.dcsys.model.MailChangesManager mailChangesManager;
	private final ar.com.dcsys.model.PersonsManager personsManager;
	
	@Inject
	public MailChangesManagerBean(ar.com.dcsys.model.MailChangesManager mcm, 
								  ar.com.dcsys.model.PersonsManager personsManager) {
		this.mailChangesManager = mcm;
		this.personsManager = personsManager;
	}
	
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(MailChange mail, Person person,	Receiver<Void> receiver) {
		try {
			mailChangesManager.persist(person, mail);
			receiver.onSuccess(null);
		} catch (PersonException e) {
			receiver.onError(e.getMessage());
		}
		
	}

	@Override
	public void remove(MailChange mail, Receiver<Void> receiver) {
		
		try {
			if (mail.getToken() == null) {
				mailChangesManager.remove(mail.getMail().getMail());
				receiver.onSuccess(null);
			} else {
				mailChangesManager.remove(mail);
				receiver.onSuccess(null);
			}
		} catch (PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findAllBy(Person person, Receiver<List<MailChange>> receiver) {

		try {
			// obtengo todos los cambios pendientes de mails
			List<MailChange> changes = mailChangesManager.findAllBy(person);
			
			// obtengo todos los mails confirmados.
			String personId = person.getId();
			List<Mail> mails = personsManager.findAllMails(personId);
			for (Mail m : mails) {
				MailChange mc = new MailChangeBean();
				mc.setMail(m);
				mc.setPersonId(personId);
				mc.setConfirmed(true);
				changes.add(mc);
			}
			
		} catch (PersonException e) {
			receiver.onError(e.getMessage());
		}
		
	}

}
