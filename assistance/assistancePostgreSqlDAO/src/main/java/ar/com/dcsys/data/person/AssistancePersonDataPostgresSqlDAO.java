package ar.com.dcsys.data.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.assistance.AssistancePersonDataException;
import ar.com.dcsys.assistance.entities.AssistancePersonData;
import ar.com.dcsys.assistance.entities.AssistancePersonDataBean;
import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.exceptions.PersonException;


public class AssistancePersonDataPostgresSqlDAO implements AssistancePersonDataDAO {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(AssistancePersonDataPostgresSqlDAO.class.getName());
	private final PostgresSqlConnectionProvider cp;
	
	private final Params params;
	
	@Inject
	public AssistancePersonDataPostgresSqlDAO(PostgresSqlConnectionProvider cp, Params params) {
		this.params = params;
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		createTables();
	}
	
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists assistancepersondata ( id varchar not null primary key, " +
																			"person_id varchar not null, " +
																			"notes varchar )");
				try {
					st.execute();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private AssistancePersonData loadAssitancePersonData(ResultSet rs) throws SQLException, PersonException {

		AssistancePersonData data = new AssistancePersonDataBean();
		
		String person_id = rs.getString("person_id");
		Person person = params.findPersonById(person_id);
		
		
		data.setId(rs.getString("id"));
		data.setNotes(rs.getString("notes"));
		data.setPerson(person);
		
		return data;
	}

	@Override
	public List<AssistancePersonData> findAll()	throws AssistancePersonDataException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id, person_id, notes from assistancepersondata";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<AssistancePersonData> assistancePersonsData = new ArrayList<AssistancePersonData>();
						while (rs.next()) {
							AssistancePersonData data = loadAssitancePersonData(rs);
							assistancePersonsData.add(data);
						}
						return assistancePersonsData;
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
			throw new AssistancePersonDataException(e);
		}
	}

	@Override
	public AssistancePersonData findById(String id) throws AssistancePersonDataException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id, person_id, notes from assistancepersondata where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							AssistancePersonData data = loadAssitancePersonData(rs);
							return data;
						} else {
							return null;
						}
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
			throw new AssistancePersonDataException(e);
		}
	}

	@Override
	public AssistancePersonData findBy(Person person) throws AssistancePersonDataException, PersonException {		
		if (person.getId() == null) {
			throw new AssistancePersonDataException("person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id, person_id, notes from assistancepersondata where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,person.getId());
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							AssistancePersonData data = loadAssitancePersonData(rs);
							return data;
						} else {
							return null;
						}
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
			throw new AssistancePersonDataException(e);
		}
	}
	

	@Override
	public String persist(AssistancePersonData data) throws AssistancePersonDataException {
		if (data == null) {
			logger.log(Level.SEVERE, "data == null");
			throw new AssistancePersonDataException("data == null");
		}
		if (data.getPerson() == null) {
			logger.log(Level.SEVERE, "person == null");
			throw new AssistancePersonDataException("person == null");			
		}
		String personId = data.getPerson().getId();
		if (personId == null) {
			logger.log(Level.SEVERE, "personId == null");
			throw new AssistancePersonDataException("personId == null");			
		}		
		
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (data.getId() == null) {
					String id = UUID.randomUUID().toString();
					data.setId(id);
					query = "insert into assistancepersondata (person_id, notes, id) values (?,?,?)";			
				} else {
					query = "update assistancepersondata set person_id = ?, notes = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, personId);
					st.setString(2, data.getNotes());
					st.setString(3, data.getId());
					
					st.executeUpdate();
					return data.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new AssistancePersonDataException(e);
		}
	}	
}
