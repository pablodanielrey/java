package ar.com.dcsys.model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

@Singleton
public class MailChangesManagerBean implements MailChangesManager {

	private final MailChangeDAO mailChangeDAO;
	
	@Inject
	public MailChangesManagerBean(MailChangeDAO mailChangeDAO) {
		this.mailChangeDAO = mailChangeDAO;
	}
	
	@Override
	public void persist(Person person, MailChange change) throws PersonException {
		mailChangeDAO.persist(person, change);
	}
	
	@Override
	public void remove(MailChange change) throws PersonException {
		mailChangeDAO.remove(change);
	}
	
	@Override
	public List<MailChange> findAllBy(Person person) throws PersonException {
		return mailChangeDAO.findAllBy(person);
	}
	
	
}
