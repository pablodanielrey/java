package ar.com.dcsys.data.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.exceptions.PersonException;


public class MailChangePostgreSqlDAO implements MailChangeDAO {

	private static final Logger logger = Logger.getLogger(MailChangePostgreSqlDAO.class.getName());
	
	private final PostgresSqlConnectionProvider cp;
	
	@Inject
	public MailChangePostgreSqlDAO(PersonDAO personDAO, PostgresSqlConnectionProvider cp) {
		this.cp = cp;
	}
	
	
	private MailChange getMailChange(ResultSet rs) throws SQLException {
		
		MailChange mc = new MailChangeBean();
		
		String mail = rs.getString("mail");
		Mail email = new MailBean();
		email.setMail(mail);
		mc.setMail(email);
		
		boolean b = rs.getBoolean("confirmed");
		mc.setConfirmed(b);
		
		String token = rs.getString("token");
		mc.setToken(token);
		
		
		String personId = rs.getString("person_id");
		mc.setPersonId(personId);
		
		return mc;
	}
	
	
	@PostConstruct
	void init() {
		createTables();
	}
	
	
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists persons_mailChanges (person_id varchar not null," +
																			 "mail varchar not null," +
																			 "confirmed boolean default false," +
																			 "token varchar not null primary key" +
																			 ")");
				try {
					st.execute();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}
	
	@Override
	public void persist(Person person, MailChange change) throws PersonException {
		
		if (person.getId() == null) {
			throw new PersonException("person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "insert into persons_mailChanges (person_id, mail, confirmed, token) values (?,?,?,?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, person.getId());
					st.setString(2, change.getMail().getMail());
					st.setBoolean(3, change.isConfirmed());
					st.setString(4, change.getToken());
					st.execute();
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			throw new PersonException(e);
		}

	}
	
	@Override
	public void remove(MailChange change) throws PersonException {
		
		if (change.getToken() == null) {
			throw new PersonException("MailChange.token == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "delete from persons_mailChanges where token = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, change.getToken());
					st.execute();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}
		
	}

	@Override
	public List<MailChange> findAllBy(Person person) throws PersonException {
		
		if (person.getId() == null) {
			throw new PersonException("person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from persons_mailChanges where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, person.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<MailChange> changes = new ArrayList<MailChange>();
						while (rs.next()) {
							MailChange mc = getMailChange(rs);
							changes.add(mc);
						}
						return changes;
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
			throw new PersonException(e);
		}
	}

	
	@Override
	public MailChange findByToken(String token) throws PersonException {
		if (token == null) {
			throw new PersonException("token == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from persons_mailChanges where token = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, token);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							MailChange mc = getMailChange(rs);
							return mc;
						} else {
							throw new PersonException("token not found");
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
			throw new PersonException(e);
		}
	}
	
}
