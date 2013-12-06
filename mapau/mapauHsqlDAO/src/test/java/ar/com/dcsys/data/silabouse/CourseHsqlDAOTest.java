package ar.com.dcsys.data.silabouse;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.DatabasePersistenceData;
import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.silabouse.CourseDAO.Params;
import ar.com.dcsys.exceptions.MapauException;

public class CourseHsqlDAOTest {
	
	private static CourseHsqlDAO dao;
	
	@BeforeClass
	public static void setEnvironment() {
		DatabasePersistenceData data = new DatabasePersistenceData();
		HsqlConnectionProvider cp = new HsqlConnectionProvider(data);		
		
		CourseDAO.Params params = new Params() {

			@Override
			public Subject findSubjectById(String id) throws MapauException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String persist(AssignableUnit au) throws MapauException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String findTypeAssignableUnit(AssignableUnit au)	throws MapauException {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		dao = new CourseHsqlDAO(cp,params);
	}
	
	@Test
	public void testCreateTables() throws SQLException {
		dao.createTables();
	}

}
