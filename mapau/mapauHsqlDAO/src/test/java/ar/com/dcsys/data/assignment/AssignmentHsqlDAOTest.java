package ar.com.dcsys.data.assignment;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.DatabasePersistenceData;
import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.assignment.AssignmentDAO.Params;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class AssignmentHsqlDAOTest {
	private static AssignmentHsqlDAO dao;
	
	@BeforeClass
	public static void setEnvironment() {
		DatabasePersistenceData data = new DatabasePersistenceData();
		HsqlConnectionProvider cp = new HsqlConnectionProvider(data);
		
		AssignmentDAO.Params params = new Params() {

			@Override
			public Person findPersonBySilegIdentifiers(String id)
					throws PersonException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Person findPersonByUsername(String username)
					throws PersonException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Person findPersonById(String id) throws PersonException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Course findCourseById(String id) throws MapauException {
				// TODO Auto-generated method stub
				return null;
			}

			
		};
		
		dao = new AssignmentHsqlDAO(cp,params);
	}
	
	@Test
	public void testCreateTables() throws SQLException {
		dao.createTables();
	}
}
