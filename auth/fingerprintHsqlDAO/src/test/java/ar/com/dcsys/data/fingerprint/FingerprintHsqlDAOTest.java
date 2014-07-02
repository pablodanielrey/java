package ar.com.dcsys.data.fingerprint;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.UUID;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.security.Finger;
import ar.com.dcsys.security.FingerprintCredentials;

public class FingerprintHsqlDAOTest {

	@Inject FingerprintHsqlDAO dao;
	
/*
	@BeforeClass
	public static void setEnvironment() {
		
		FingerprintDAO.Params params = new Params() {
			@Override
			public Person findPersonById(String arg0) throws PersonException {
				return null;
			}
		};
		dao = new FingerprintHsqlDAO(cp,params);
	}
	*/
	
//	@Test
	public void testCreateTables() throws SQLException {
		dao.createTables();
	}
	
//	@Test
	public void testPersistFingerprint() throws SQLException, FingerprintException {
		dao.createTables();
		
		FingerprintCredentials fpc = createCredentials();
		Person person = createPerson();
		
		Fingerprint fp = new Fingerprint();
		fp.setFingerprint(fpc);
		fp.setPerson(person);
		dao.persist(fp);
		
	}


//	@Test
	public void testFindById() throws SQLException, PersonException, FingerprintException {
		dao.createTables();
		
		FingerprintCredentials fpc = createCredentials();
		Fingerprint fp = dao.findByFingerprint(fpc);
		
		assertNotNull(fp);
		assertNotNull(fp.getId());
		assertNotNull(fp.getFingerprint());
		
	}
	
	
	private static Person createPerson() {
		Person person = new Person();
		person.setId(UUID.randomUUID().toString());
		person.setName("Pablo Daniel");
		person.setLastName("Rey");
		person.setDni("1234567");
		return person;
	}
	
	
	private static FingerprintCredentials createCredentials() {
		FingerprintCredentials fpc = new FingerprintCredentials();
		fpc.setAlgorithm("algoritmo");
		fpc.setCodification("codificacion");
		fpc.setFinger(Finger.LEFT_INDEX);
		
		final int maxValue = 498;
		byte[] temp = new byte[maxValue];
		for (int i = 0; i < maxValue; i++) {
			temp[i] = (byte)i;
		}
		fpc.setTemplate(temp);

		return fpc;
	}
	
}
