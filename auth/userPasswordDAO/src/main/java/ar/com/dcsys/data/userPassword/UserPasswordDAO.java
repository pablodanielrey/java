package ar.com.dcsys.data.userPassword;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.exceptions.UserPasswordException;

public interface UserPasswordDAO {
	
	public UserPassword findById(String id) throws UserPasswordException, PersonException;
	public List<UserPassword> findByPerson(Person person) throws UserPasswordException, PersonException;
	
	public String persist(UserPassword userPassword) throws UserPasswordException;
	
}
