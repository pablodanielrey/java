package ar.com.dcsys.server.tutoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.StudentDataManager;
import ar.com.dcsys.model.auth.AuthManager;
import ar.com.dcsys.persistence.JdbcConnectionProvider;


public class TutoriaManager {
	
	private final static Logger logger = Logger.getLogger(TutoriaManager.class.getName());
	
//	@Resource SessionContext ctx;
	
	private final StudentDataManager studentDataManager;
	private final AuthManager authsManager;
	private final PersonsManager personsManager;
	private final JdbcConnectionProvider cp;
	
	@Inject
	public TutoriaManager(StudentDataManager studentDataManager, PersonsManager personsManager, AuthManager authManager, JdbcConnectionProvider cp) {
		this.studentDataManager = studentDataManager;
		this.personsManager = personsManager;
		this.authsManager = authManager;
		this.cp = cp;
	}
	
	@PostConstruct
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists tutoria (id varchar not null primary key, " +
																				  "date timestamp not null, " +
																				  "person_id varchar, " +
																				  "type varchar not null," +
																				  "date_log timestamp not null," +
																				  "owner_id varchar not null)");
				try {
					st.executeUpdate();
					
				} finally {
					st.close();
				}

			} finally {
				cp.closeConnection(con);
			}
		
		} catch (SQLException e) {
			logger.log(Level.SEVERE,"No se puede agregar el registro en la base de tutorias : " + e.getMessage(),e);
		}		
	}
	


	public void add(String personId, Date date, String studentNumber, String type) throws TutoriaException {
		
		try {
			String studentId = null;
			if (studentNumber != null) {
				studentId = studentDataManager.fingIdByStudentNumber(studentNumber);
			}
			
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into tutoria (id, date, person_id, type, date_log, owner_id) values (?,?,?,?,?,?)");
				try {
					st.setString(1,UUID.randomUUID().toString());
					st.setTimestamp(2,new Timestamp(date.getTime()));
					st.setString(3,studentId);
					st.setString(4,type);
					st.setTimestamp(5,new Timestamp(new Date().getTime()));
					st.setString(6,personId);
					st.executeUpdate();
					
				} finally {
					st.close();
				}

			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new TutoriaException(e);

		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new TutoriaException(e);
		}
		
	}
	
	public List<TutoriaRecord> getAll() throws TutoriaException, PersonException {
		
		try {
		Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from tutoria");
				try {
					ResultSet rs = st.executeQuery();
					try {

						List<TutoriaRecord> records = new ArrayList<TutoriaRecord>();
						while (rs.next()) {
							String type = rs.getString("type");
							Person p = getPerson(rs, "person_id");
							Date date = new Date(rs.getTimestamp("date").getTime());
							Date logDate = new Date(rs.getTimestamp("date_log").getTime());
//							Person tutor = getPerson(rs,"owner_id");
							Person tutor = null;
							
							TutoriaRecord tr = new TutoriaRecord(date,p,type,logDate,tutor);
							records.add(tr);
						}
						
						return records;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
	
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new TutoriaException(e);
		}

	}
	
	private Person getPerson(ResultSet rs, String rid) throws SQLException, PersonException {
		
		String id = rs.getString(rid);
		if (id == null) {
			return null;
		}
		
		Person person = personsManager.findById(id);
		return person;
	}
	
	
}
