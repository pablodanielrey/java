package ar.com.dcsys.data.silabouse;

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

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.exceptions.MapauException;

public class SubjectHsqlDAO implements SubjectDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(SubjectHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	
	@Inject
	public SubjectHsqlDAO(HsqlConnectionProvider cp) {
		this.cp = cp;
	}

	@PostConstruct
	void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists subject (" +
															   	"id longvarchar NOT NULL PRIMARY KEY," +
													   			"name longvarchar NOT NULL," +
													   			"version BIGINT NULL);");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}

	private Subject getSubject(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String name = resultSet.getString("name");
		Long version = resultSet.getLong("version");
		
		Subject subject = new SubjectBean();
   		subject.setId(id);
   		subject.setName(name);
   		subject.setVersion(version);
   		
   		return subject;
	}	

	@Override
	public Subject findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM subject WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Subject subject = getSubject(rs);
							return subject;
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
				con.close();
			}
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public List<Subject> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM subject";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Subject> subjects = new ArrayList<Subject>();
						while (rs.next()) {
							Subject subject = getSubject(rs);
							subjects.add(subject);
						}
						return subjects;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public String persist(Subject subject) throws MapauException {
		if (subject == null) {
			throw new MapauException("subject == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (subject.getId() == null) {
					String id = UUID.randomUUID().toString();
					subject.setId(id);
					query = "INSERT INTO subject (name, version, id) VALUES (?, ?, ?);";
				} else {
					query = "UPDATE subject SET name = ?, version = ? WHERE subject.id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
				   	st.setString(1, subject.getName());
				   	st.setLong(2, subject.getVersion());
					st.setString(3, subject.getId());
				   	
				   	st.executeUpdate();
				   	
					return subject.getId();					
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}

}
