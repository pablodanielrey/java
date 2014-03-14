package ar.com.dcsys.data.person;

import java.util.List;

import ar.com.dcsys.exceptions.PersonException;

public class MailChangeLdapDAO implements MailChangeDAO {

	@Override
	public void persist(Person person, MailChange change) throws PersonException {
		throw new PersonException("not implemented");
	}

	@Override
	public void remove(MailChange change) throws PersonException {
		throw new PersonException("not implemented");
	}

	@Override
	public List<MailChange> findAllBy(Person person) throws PersonException {
		throw new PersonException("not implemented");
	}

	@Override
	public MailChange findByToken(String token) throws PersonException {
		throw new PersonException("not implemented");
	}

	@Override
	public List<MailChange> findByMail(String mail) throws PersonException {
		throw new PersonException("not implemented");
	}

}
