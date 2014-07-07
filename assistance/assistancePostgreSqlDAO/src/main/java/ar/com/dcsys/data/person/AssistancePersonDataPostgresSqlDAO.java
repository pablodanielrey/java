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

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.exceptions.PersonDataException;
import ar.com.dcsys.exceptions.PersonException;


public class AssistancePersonDataPostgresSqlDAO implements AssistancePersonDataDAO {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(AssistancePersonDataPostgresSqlDAO.class.getName());
	private final PostgresSqlConnectionProvider cp;
	
	@Inject
	public AssistancePersonDataPostgresSqlDAO(PostgresSqlConnectionProvider cp) {
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
				PreparedStatement st = con.prepareStatement("create table if not exists assistance_persondata ( id varchar not null primary key, " +
																			"pin varchar not null," + 
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

		AssistancePersonData data = new AssistancePersonData();
		
		data.setId(rs.getString("id"));
		data.setPin(rs.getString("pin"));
		data.setNotes(rs.getString("notes"));
		
		return data;
	}

	@Override
	public List<AssistancePersonData> findAll()	throws PersonDataException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from assistance_persondata";
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
			throw new PersonDataException(e);
		}
	}

	@Override
	public AssistancePersonData findById(String id) throws PersonDataException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from assistance_persondata where id = ?";
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
			throw new PersonDataException(e);
		}
	}

	@Override
	public String persist(AssistancePersonData data) throws PersonDataException {
		if (data == null) {
			logger.log(Level.SEVERE, "data == null");
			throw new PersonDataException("data == null");
		}
		
		String personId = data.getId();
		if (personId == null) {
			logger.log(Level.SEVERE, "person == null");
			throw new PersonDataException("person == null");			
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "update assistance_persondata set pin = ?, notes = ? where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, data.getPin());
					st.setString(2, data.getNotes());
					st.setString(3, data.getId());
					
					if (st.executeUpdate() <= 0) {
						query = "insert into assistance_persondata (pin, notes, id) values (?,?,?)";	
						st = con.prepareStatement(query);
						st.setString(1, data.getPin());
						st.setString(2, data.getNotes());
						st.setString(3, data.getId());
						st.executeUpdate();
					}
					
					return data.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonDataException(e);
		}
	}	
}
