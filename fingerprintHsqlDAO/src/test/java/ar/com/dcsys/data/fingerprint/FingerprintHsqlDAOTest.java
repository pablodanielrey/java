package ar.com.dcsys.data.fingerprint;

import java.sql.SQLException;

import ar.com.dcsys.data.DatabasePersistenceData;
import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.fingerprint.FingerprintDAO.Params;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public class FingerprintHsqlDAOTest {

	public void testCreateTables() throws SQLException {
		
		DatabasePersistenceData data = new DatabasePersistenceData();
		HsqlConnectionProvider cp = new HsqlConnectionProvider(data);
		
		FingerprintDAO.Params params = new Params() {
			@Override
			public Person findPersonById(String arg0) throws PersonException {
				return null;
			}
		};
		FingerprintHsqlDAO dao = new FingerprintHsqlDAO(cp,params);
		
		dao.createTables();
		
	}
	
}
