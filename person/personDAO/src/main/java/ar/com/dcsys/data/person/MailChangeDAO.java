package ar.com.dcsys.data.person;

import java.util.List;

import ar.com.dcsys.exceptions.PersonException;

public interface MailChangeDAO {

	public void persist(Person person, MailChange change) throws PersonException;
	public void remove(MailChange change) throws PersonException;
	public List<MailChange> findAllBy(Person person) throws PersonException;
	public MailChange findByToken(String token) throws PersonException;
	public List<MailChange> findByMail(String mail) throws PersonException;
	
}
