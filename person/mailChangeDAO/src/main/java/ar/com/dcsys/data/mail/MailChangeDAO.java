package ar.com.dcsys.data.mail;

import java.util.List;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.exceptions.PersonException;

public interface MailChangeDAO {

	public void persist(MailChange change) throws PersonException;
	public void remove(MailChange change) throws PersonException;
	public List<MailChange> findAllBy(String personId) throws PersonException;
	public MailChange findByToken(String token) throws PersonException;
	public List<MailChange> findByMail(String mail) throws PersonException;
	
}
