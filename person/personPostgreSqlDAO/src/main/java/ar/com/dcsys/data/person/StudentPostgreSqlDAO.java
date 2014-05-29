package ar.com.dcsys.data.person;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import javax.inject.Inject;

public class StudentPostgreSqlDAO implements StudentDataDAO {
	
	private static final Logger logger = Logger.getLogger(StudentPostgreSqlDAO.class.getName());
	
	private final PostgresSqlConnectionProvider cp;

	@Inject
	public StudentPostgreSqlDAO(PostgresSqlConnectionProvider cp) {
		this.cp = cp;
	}
	
	@Override
	public String persist(StudentData sd) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StudentData findById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudentData> findAll() throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findByStudentNumber(String sn) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

}
