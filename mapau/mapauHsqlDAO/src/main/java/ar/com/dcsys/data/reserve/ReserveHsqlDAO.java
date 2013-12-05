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
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveHsqlDAO implements ReserveDAO {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(ReserveHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public ReserveHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists reserve (" +
																"id longvarchar not null primary key, " +
																"description longvarchar," +
																"owner_id longvarchar not null," +
																"created timestamp not null," +
																"related_id longvarchar," +
																"classroom_id longvarchar not null ," +
																"reserveattemptdate_id longvarchar not null ," +
																"version bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists reserve_person (" +
												"reserve_id longvarchar not null references reserve (id)," +
												"person_id longvarchar not null )");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	private void loadRelatedPersons(Connection con, Reserve res) throws SQLException, PersonException {
		String query = "select person_id from reserve_person where reserve_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, res.getId());
			ResultSet rs = st.executeQuery();
			try {
				List<Person> related = new ArrayList<>();
				while (rs.next()) {
					String id = rs.getString("person_id");
					Person person = params.findPersonById(id);
					related.add(person);
				}
				res.setRelatedPersons(related);
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}	
	
	private Reserve getReserve(ResultSet rs) throws SQLException, PersonException, MapauException {
		String id = rs.getString("id");
		Date created = new Date(rs.getTimestamp("created").getTime());
		String description  = rs.getString("description");
		Long version = rs.getLong("version");
		
		String person_id = rs.getString("owner_id");
		Person owner = params.findPersonById(person_id);
		
		String classRoom_id = rs.getString("classroom_id");
		ClassRoom classRoom = params.findClassRoomById(classRoom_id);
		
		String reserveAttemptDate_id = rs.getString("reserveattemptdate_id");
		ReserveAttemptDate rad = params.findReserveAttemptDateById(reserveAttemptDate_id);
		
		Reserve res = new ReserveBean();

		/*
		 * TODO: por ahora no se toca el campo relacionado de la reserva.
		String reserve_id = rs.getString("related_id");
		if (reserve_id != null && (!(reserve_id.trim().equals(""))) && (!(reserve_id.equals(id)))) {			// se chequea un simple loop infinito tambien.
			Reserve related = params.findReserveById(reserve_id);
			res.setRelated(related);
		}
		*/
		
		res.setDescription(description);
		res.setVersion(version);
		res.setId(id);
		res.setCreated(created);
		res.setOwner(owner);
		res.setClassRoom(classRoom);
		res.setReserveAttemptDate(rad);
		
		return res;
	}	

	@Override
	public Reserve findById(String id) throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM reserve WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Reserve r = getReserve(rs);
							loadRelatedPersons(con, r);
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
		} catch(SQLException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<Reserve> findAll() throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM reserve";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Reserve> reserves = new ArrayList<Reserve>();
						while (rs.next()) {
							Reserve r = getReserve(rs);
							loadRelatedPersons(con, r);
							reserves.add(r);
						}
						return reserves;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch(SQLException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<Reserve> findBy(Date start, Date end) throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM reserve AS r WHERE (r.rstart >= ?) AND (r.rend <= ?);";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(start.getTime()));
					st.setTimestamp(2, new Timestamp(end.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<Reserve> reserves = new ArrayList<Reserve>();
						while (rs.next()) {
							Reserve r = getReserve(rs);
							loadRelatedPersons(con, r);
							reserves.add(r);
						}
						return reserves;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch(SQLException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<Reserve> findReserveRelatedWithId(String id) throws MapauException {
		throw new MapauException("Not Implemented");
	}

	@Override
	public List<Reserve> findBy(ReserveAttemptDate date) throws MapauException,	PersonException {
		if (date == null) {
			throw new MapauException("reserveAttemptDate == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from reserve where reserveattemptdate_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, date.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<Reserve> reserves = new ArrayList<Reserve>();
						while (rs.next()) {
							Reserve r = getReserve(rs);
							loadRelatedPersons(con, r);
							reserves.add(r);
						}
						return reserves;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch(SQLException e) {
			throw new MapauException(e);
		}
	}
	
	private boolean include(List<Reserve> reserves, Reserve reserve) {
		for (Reserve r : reserves) {
			if (reserve.getId().equals(r.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Reserve> findAllCollidingWith(Date start, Date end, List<ClassRoom> classRooms) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String[] classRoomsS = new String[classRooms.size()];
				for (int i = 0; i < classRoomsS.length; i++) {
					classRoomsS[i] = classRooms.get(i).getId();
				}
				
				/**
				 * La consulta original sería :
				 * 
				 * (start between rstart and rend) OR (end between rstart and rend) OR (start < rstart and end > rend) OR (start > rstart and end < rend)
				 * 
				 * o mas simple 
				 * 
				 * NOT (rstart < start and rend < start) OR (rstart > end and rend > end)
				 * 
				 * =>
				 * 
				 * (rstart > start or rend > start) AND (rstart < end or rend < end)
				 * 
				 */
							
				String query = "select * from reserve r where (rstart > ? or rend > ?) AND (rstart < ? or rend < ?) AND (classroom_id = ?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					List<Reserve> reserves = new ArrayList<Reserve>();
					
					for (String classRoomId : classRoomsS) {
						st.setTimestamp(1, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(2, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(3, new java.sql.Timestamp(end.getTime()));
						st.setTimestamp(4, new java.sql.Timestamp(end.getTime()));
						st.setString(5, classRoomId);
						
						ResultSet rs = st.executeQuery();
						try {
							while (rs.next()) {
								Reserve r = getReserve(rs);
								loadRelatedPersons(con, r);
								if (!include(reserves,r)) {
									reserves.add(r);
								}
							}
						} finally {
							rs.close();
						}
					}
					return reserves;
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch(SQLException | PersonException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<Reserve> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException {
		if (dates == null) {
			throw new MapauException("reserveAttemptDate == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from reserve r where (rstart > ? or rend > ?) AND (rstart < ? or rend < ?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					List<Reserve> reserves = new ArrayList<Reserve>();
					for (DatesRange dr : dates) {
						Date start = dr.getStart();
						Date end = dr.getEnd();
						
						st.setTimestamp(1, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(2, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(3, new java.sql.Timestamp(end.getTime()));
						st.setTimestamp(4, new java.sql.Timestamp(end.getTime()));

						ResultSet rs = st.executeQuery();
						try {
							while (rs.next()) {
								Reserve res = getReserve(rs);
								loadRelatedPersons(con, res);
								reserves.add(res);
							}	
						} finally {
							rs.close();
						}
					}
					return reserves;
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch(SQLException e) {
			throw new MapauException(e);
		}
	}


	@Override
	public String persist(Reserve reserve) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (reserve.getId() == null) {
					String id = UUID.randomUUID().toString();
					reserve.setId(id);
					query = "INSERT INTO reserve (description, version, created, owner_id, related_id, classroom_id, reserveattemptdate_id, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
				} else {
					query = "UPDATE reserve SET description = ?, version = ?, created = ?, owner_id = ?, related_id = ?, classroom_id = ?, reserveattemptdate_id = ? WHERE reserve.id = ?;";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, reserve.getDescription());
					st.setLong(2, reserve.getVersion());
					st.setTimestamp(3, new Timestamp(reserve.getCreated().getTime()));
					st.setString(4,reserve.getOwner().getId());
					
					/*
					 * TODO: por ahora no se toca el campo relacionado de la reserva.
					String related_id = object.getRelated() == null ? null : object.getRelated().getId();
					pstmt.setString(5,related_id);
					*/
					st.setString(5,"RESERVADO");
					
					st.setString(6, reserve.getClassRoom().getId());
					st.setString(7, reserve.getReserveAttemptDate().getId());
					st.setString(8, reserve.getId());
				   	
				   	st.executeUpdate();
				   	
				   	persistPersons(con,reserve);
				   	
					return reserve.getId();
					
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
	
	private void persistPersons(Connection con, Reserve reserve) throws SQLException {
		removePersons(con, reserve);
		
		List<Person> persons = reserve.getRelatedPersons();
		
		String query = "INSERT INTO reserve_person (reserve_id, person_id) VALUES (?, ?);";
		PreparedStatement st = con.prepareStatement(query);
		try {
			for (Person person : persons) {
				st.setString(1, reserve.getId());
				st.setString(2, person.getId());
				st.executeUpdate();
			}
		} finally {
			st.close();
		}
	}

	private void removePersons(Connection con, Reserve reserve) throws SQLException {
		String query = "delete from reserve_person where reserve_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, reserve.getId());
			st.executeUpdate();
		} finally {
			st.close();
		}
	}
	
	@Override
	public void remove(Reserve reserve) throws MapauException {
		if (reserve == null) {
			throw new MapauException("reserve == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				removePersons(con, reserve);
				
				String query = "DELETE FROM reserve WHERE id = ?;";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, reserve.getId());
					st.executeUpdate();
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
