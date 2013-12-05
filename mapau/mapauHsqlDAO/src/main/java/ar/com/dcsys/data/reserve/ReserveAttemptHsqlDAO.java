package ar.com.dcsys.data.reserve;

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

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveAttemptHsqlDAO implements ReserveAttemptDAO {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(ReserveAttemptHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public ReserveAttemptHsqlDAO(HsqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@PostConstruct
	void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists reserveattempt (" +
																"id longvarchar not null primary key, " +
																"description longvarchar," +
																"owner_id longvarchar not null," +
													   			"created timestamp not null," +
																"version bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists reserveattemptdeleted (" +
												"id longvarchar not null primary key, " +
												"reserveattempt_id longvarchar not null ," +
												"description longvarchar," +
												"notes text not null," +	
												"version bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists reserveattempt_person (" +
												"reserveattempt_id longvarchar NOT NULL REFERENCES reserveattempt (id )," +
												"person_id longvarchar NOT NULL)");		
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	
	private ReserveAttempt getReserveAttempt(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
   		String description = resultSet.getString("description");
   		Date created = resultSet.getTimestamp("created");
   		Long version = resultSet.getLong("version");		
   		
   		ReserveAttempt reserveAttempt = new ReserveAttemptBean();
   		reserveAttempt.setId(id);
   		reserveAttempt.setDescription(description);
   		reserveAttempt.setCreated(created);
   		reserveAttempt.setVersion(version);
   		
   		return reserveAttempt;
	}
	
	private ReserveAttemptDeleted getReserveAttemptDeleted(ResultSet rs) throws SQLException {
		
		String id = rs.getString("id");
		String description = rs.getString("description");
		String notes = rs.getString("notes");
		Long version = rs.getLong("version");
		
		ReserveAttemptDeleted reserveAttemptDeleted = new ReserveAttemptDeletedBean();
		reserveAttemptDeleted.setId(id);
		reserveAttemptDeleted.setDescription(description);
		reserveAttemptDeleted.setNotes(notes);
		reserveAttemptDeleted.setVersion(version);
		
		return reserveAttemptDeleted;
	}		
	
	private void loadPersons(Connection connection, ReserveAttempt ra) throws SQLException, PersonException {
	   	PreparedStatement st = connection.prepareStatement("SELECT person_id FROM reserveattempt_person ra_p WHERE ra_p.reserveAttempt_id = ?");
	   	try {
		   	st.setString(1, ra.getId());		   	
		   	ResultSet rs = st.executeQuery();
		   	try {
				while(rs.next()){
					String id = rs.getString("person_id");
					Person person = params.findPersonById(id);
					ra.getRelatedPersons().add(person);
			   	}
		   	} finally {
				rs.close();
			}
	   	} finally {
	   		st.close();
	   	}
		 
	}	

	@Override
	public ReserveAttemptDeleted findReserveAttemptDeletedById(String id) throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from reserveattemptdeleted r where r.id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ReserveAttemptDeleted r = getReserveAttemptDeleted(rs);
							return r;
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
	public ReserveAttempt findById(String id) throws MapauException,PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM reserveattempt r WHERE r.id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ReserveAttempt r = getReserveAttempt(rs);
							loadPersons(con, r);
							return r;
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
	public List<ReserveAttempt> findAll() throws MapauException,PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from reserveAttempt";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<ReserveAttempt> reserveAttempts = new ArrayList<ReserveAttempt>();
						while (rs.next()) {
							ReserveAttempt r = getReserveAttempt(rs);
							loadPersons(con, r);
							reserveAttempts.add(r);
						}
						return reserveAttempts;
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
	public List<ReserveAttempt> findStandingReserveAttemptsBy(Area area) throws MapauException, PersonException {
		if (area == null) {
			throw new MapauException("Area == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from (select * from reserveattempt where area_id = ?) as r " +
						   "where r.commited is false and not exists (select * from reserveattemptdeleted rad where r.id = rad.reserveattempt_id)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, area.getId());
					ResultSet rs = st.executeQuery();
					try {						
						List<ReserveAttempt> reserveAttempts = new ArrayList<ReserveAttempt>();
						while (rs.next()) {
							ReserveAttempt r = getReserveAttempt(rs);
							loadPersons(con, r);
							reserveAttempts.add(r);
						}
						return reserveAttempts;
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

	/**
	 * TODO: pasarlo para el backend de reserve.
	 * El estado Commited = true implica que todas las fechas de la reserva fueron reservadas.
	 * si existe alguna que no lo fue entonces NO puede estar commited = true.
	 */	
	@Override
	public boolean updateCommited(ReserveAttempt ra) throws MapauException {
		throw new MapauException("FALTA REIMPLEMENTAR EN EL MODELO CAPA 1");
	}

	@Override
	public String persist(ReserveAttempt reserveAttempt) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (reserveAttempt.getId() == null) {
					String id = UUID.randomUUID().toString();
					reserveAttempt.setId(id);
					query = "INSERT INTO reserveattempt (description, version, created, owner_id, id) VALUES (?, ?, ?, ?, ?);";
				} else {
					query = "UPDATE reserveattempt SET description = ?, version = ?, created = ?, owner_id = ? WHERE reserveAttempt.id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, reserveAttempt.getDescription());
				   	st.setLong(2, reserveAttempt.getVersion());
				   	st.setTimestamp(3, new Timestamp(reserveAttempt.getCreated().getTime()));
					st.setString(4, reserveAttempt.getOwner().getId());
					st.setString(5, reserveAttempt.getId());
				   	
					st.executeUpdate();					
					
					return reserveAttempt.getId();
					
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
	public String persist(ReserveAttemptDeleted r) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				if (r.getId() == null) {
					String id = UUID.randomUUID().toString();
					r.setId(id);
				}
				String query = "insert into reserveattemptdeleted (id, notes, description, reserveattempt_id, version) values (?,?,?,?,?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, r.getId());
					st.setString(2, r.getNotes());
					st.setString(3, r.getDescription());
					st.setString(4, r.getReserveAttempt().getId());
					st.setLong(5,r.getVersion());
					
					st.executeUpdate();			
					
					return r.getId();
					
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
	public void remove(ReserveAttempt object) throws MapauException {
		throw new MapauException("Not Implemented");
	}

}
