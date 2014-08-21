package ar.com.dcsys.data.log;

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

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;

public class AttLogPostgresSqlDAO implements AttLogDAO {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AttLogPostgresSqlDAO.class.getName());

	private final PostgresSqlConnectionProvider cp;
	private final Params params;
	

	@Inject
	public AttLogPostgresSqlDAO(PostgresSqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@PostConstruct
	public void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	public void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists attLog (" +
																"id varchar not null primary key, " +
																"device_id varchar not null," +
																"person_id varchar not null, " +
																"verifyMode bigint not null, " +
																"date timestamp not null)");
			try {
				st.executeUpdate();
				
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}		
	}	
	
	
	private AttLog getAttLog(ResultSet rs) throws SQLException, DeviceException {
		AttLog log = new AttLog();
		log.setId(rs.getString("id"));
		log.setDate(new Date(rs.getTimestamp("date").getTime()));
		log.setVerifyMode(rs.getLong("verifyMode"));
		return log;
	}
	
	private void loadPerson(ResultSet rs, AttLog log) throws SQLException, PersonException {
		String person_id = rs.getString("person_id"); 
		log.setPerson(params.findPersonById(person_id));		
	}
	
	private void loadDevice(ResultSet rs, AttLog log) throws SQLException, DeviceException {
		String device_id = rs.getString("device_id");
		log.setDevice(params.findDeviceById(device_id));
	}
	
	
	@Override
	public void remove(String id) throws AttLogException {
		try {
			Connection	con = cp.getConnection();
			try {
				String query = "delete from attLog where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id);
					st.executeUpdate();

				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new AttLogException(e);
		}
	}
	
	@Override
	public AttLog findById(String id) throws AttLogException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from attLog where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							AttLog log = getAttLog(rs);
							loadPerson(rs, log);
							loadDevice(rs, log);
							return log;
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
		} catch (SQLException | DeviceException e) {
			throw new AttLogException(e);
		}
	}
	
	
	@Override
	public List<String> findAll() throws AttLogException {
		try {
			Connection	con = cp.getConnection();
			try {
				String query = "select id from attLog";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> logs = new ArrayList<>();
						while (rs.next()) {
							logs.add(rs.getString("id"));
						}
						return logs;
						
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
			throw new AttLogException(e);
		}
	}
	
	@Override
	public List<AttLog> findAll(Date start, Date end) throws AttLogException, PersonException {
		try {
			Connection	con = cp.getConnection();
			try {
				String query = "select * from attLog where date >= ? and date <= ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1,new Timestamp(start.getTime()));
					st.setTimestamp(2,new Timestamp(end.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<AttLog> logs = new ArrayList<>();
						while (rs.next()) {
							AttLog l = getAttLog(rs);
							loadPerson(rs, l);
							loadDevice(rs, l);
							logs.add(l);
						}
						return logs;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException | DeviceException e) {
			throw new AttLogException(e);
		}
	}

	
	@Override
	public List<AttLog> findAll(Person person, Date start, Date end) throws AttLogException {
		try {
			Connection	con = cp.getConnection();
			try {
				String query = "select * from attLog where date >= ? and date <= ? and person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1,new Timestamp(start.getTime()));
					st.setTimestamp(2,new Timestamp(end.getTime()));
					st.setString(3,person.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<AttLog> logs = new ArrayList<>();
						while (rs.next()) {
							AttLog j = getAttLog(rs);
							loadDevice(rs,j);
							j.setPerson(person);
							
							logs.add(j);
						}
						return logs;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException | DeviceException e) {
			throw new AttLogException(e);
		}
	}

	
	@Override
	public Boolean findByExactDate(Person person, Date date) throws AttLogException, PersonException, DeviceException {
		
		if (person == null) {
			throw new PersonException("person == null");
		}
		
		if (date == null) {
			throw new AttLogException("date == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from attLog where (person_id = ? and date = ?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, person.getId());
					st.setTimestamp(2, new Timestamp(date.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						return (rs.next());
						
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
			throw new AttLogException(e.getMessage());
		}
	}
	
	/**
	 * Carga el id del log en la instancia si es que existe dentro de la base.
	 * @param con
	 * @param log
	 * @throws AttLogException
	 */
	private void loadId(Connection con, AttLog log) throws AttLogException {
		
		if (log.getDevice() == null) {
			throw new AttLogException("AttLog.device == null");
		}
		
		if (log.getPerson() == null) {
			throw new AttLogException("AttLog.person == null");
		}
		
		if (log.getDate() == null) {
			throw new AttLogException("AttLog.device == null");
		}
		
		if (log.getVerifyMode() == null) {
			throw new AttLogException("attLog.verifyMode == null");
		}
		
		
		try {
			String query = "select id from attLog where (person_id = ? and device_id = ? and verifyMode = ? and date = ?)";
			PreparedStatement st = con.prepareStatement(query);
			try {
				st.setString(1, log.getPerson().getId());
				st.setString(2, log.getDevice().getId());
				st.setLong(3, log.getVerifyMode());
				st.setTimestamp(4, new Timestamp(log.getDate().getTime()));
				ResultSet rs = st.executeQuery();
				try {
					if (rs.next()) {
						String id = rs.getString("id");
						log.setId(id);
					}
				} finally {
					rs.close();
				}
			} finally {
				st.close();
			}
		} catch (SQLException | NullPointerException e) {
			throw new AttLogException(e);
		}
	}
	
	
	@Override
	public void persist(AttLog log) throws AttLogException {
		
		boolean existent = false;
		if (log.getId() != null) {
			try {
				AttLog log2 = findById(log.getId());
				if (log2 != null) {
					existent = true;
				}
				
			} catch (PersonException e) {
				throw new AttLogException(e);
			}
		}
		
		
		
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (existent) {
					query = "update attLog set person_id = ?, device_id = ?, verifyMode = ?, date = ? where id = ?";
				} else {
					if (log.getId() == null) {
						loadId(con, log);
						if (log.getId() != null) {
							// existe asi que no lo agrego. retorno sin error. esta operación es idempotente.
							return;
						}
						log.setId(UUID.randomUUID().toString());
					}
					query = "insert into attLog (person_id, device_id, verifyMode, date, id) values (?,?,?,?,?)";
				}

				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, log.getPerson().getId());
					st.setString(2, log.getDevice().getId());
					st.setLong(3, log.getVerifyMode());
					st.setTimestamp(4, new Timestamp(log.getDate().getTime()));
					st.setString(5, log.getId());
					st.executeUpdate();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new AttLogException(e);
		}
	}
	
}
