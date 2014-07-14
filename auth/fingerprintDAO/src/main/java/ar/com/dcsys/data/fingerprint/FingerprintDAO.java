package ar.com.dcsys.data.fingerprint;

import java.util.List;

import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.security.Fingerprint;

public interface FingerprintDAO {

	public List<String> findAll() throws FingerprintException;
	public Fingerprint findById(String id) throws FingerprintException;

	public List<Fingerprint> findByPerson(String personId) throws FingerprintException;
	
	public String persist(Fingerprint fp) throws FingerprintException;
	
}
