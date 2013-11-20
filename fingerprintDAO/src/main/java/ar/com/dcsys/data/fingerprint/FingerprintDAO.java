package ar.com.dcsys.data.fingerprint;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.security.FingerprintCredentials;

public interface FingerprintDAO {

	public Fingerprint findById(String id) throws FingerprintException;
	public Fingerprint findByFingerprint(FingerprintCredentials fp) throws FingerprintException, PersonException;
	public List<Fingerprint> findByPerson(Person person) throws FingerprintException, PersonException;
	
	public String persist(Fingerprint fp) throws FingerprintException;
	
	public interface Params {
		public Person findPersonById(String id) throws PersonException;
	}
	
}
