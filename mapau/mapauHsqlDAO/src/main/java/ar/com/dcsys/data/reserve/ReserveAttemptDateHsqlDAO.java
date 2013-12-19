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
import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.CharacteristicQuantityBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveAttemptDateHsqlDAO implements ReserveAttemptDateDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(ReserveAttemptDateHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	
	@Inject
	public ReserveAttemptDateHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists reserveattemptdate (" +
																"id longvarchar not null primary key, " +
																"related_id longvarchar," +
																"rstart timestamp not null," +
																"rend timestamp not null," +
																"creator_id longvarchar not null," +
																"created timestamp not null," +
																"course_id longvarchar," +
																"type_id longvarchar," +
																"area_id longvarchar," +
																"studentGroup longvarchar," +
																"description longvarchar," +
																"version bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists reserveattemptdate_person (" +
												"reserveattemptdate_id longvarchar NOT NULL REFERENCES reserveattemptdate (id )," +
												"person_id longvarchar NOT NULL)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists reserveattemptdate_characteristic (" +
												"reserveattemptdate_id longvarchar NOT NULL REFERENCES reserveattemptdate (id )," +
												"characteristic_id longvarchar NOT NULL," +
												"quantity bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}

	private void loadCharacteristics(Connection connection, ReserveAttemptDate rad) throws SQLException, MapauException {
		String query = "SELECT characteristic_id, quantity FROM reserveattemptdate_characteristic ra_cq WHERE ra_cq.reserveattemptdate_id = ?";
	   	PreparedStatement st = connection.prepareStatement(query);
	   	try {
		   	st.setString(1, rad.getId());
		   	ResultSet rs = st.executeQuery();
		   	try {
				while (rs.next()) {
					Long count = rs.getLong("quantity");
					String id = rs.getString("characteristic_id");
					
					CharacteristicQuantity c = new CharacteristicQuantityBean();
					c.setQuantity(count);
					c.setCharacteristic(params.findCharacteristicById(id));
					
					rad.getCharacteristicsQuantity().add(c);
			   	}
		   	} finally {
		   		rs.close();
		   	}
	   	} finally {
		   	st.close();
		}
	}	
	
	private void loadPersons(Connection connection, ReserveAttemptDate rad) throws SQLException, PersonException {
	   	String query = "SELECT person_id FROM reserveattemptdate_person ra_p WHERE ra_p.reserveattemptdate_id = ?";
	   	PreparedStatement st = connection.prepareStatement(query);
	   	try {
		   	st.setString(1, rad.getId());
		   	ResultSet rs = st.executeQuery();
		   	try {
				while(rs.next()){
					String id = rs.getString("person_id");
					Person person = params.findPersonById(id);
					rad.getRelatedPersons().add(person);
			   	}
		   	} finally {
		   		rs.close();
		   	}
	   	} finally {
	   		st.close();
	   	}
	}	
	
	private ReserveAttemptDate getReserveAttemptDate(ResultSet rs) throws SQLException, MapauException, PersonException {
		ReserveAttemptDate rd = new ReserveAttemptDateBean();
		rd.setId(rs.getString("id"));
		
		rd.setStart(rs.getTimestamp("rstart"));
		rd.setEnd(rs.getTimestamp("rend"));
		
		String creatorId = rs.getString("creator_id");
		Person creator = params.findPersonById(creatorId);
		rd.setCreator(creator);
		
		String courseId = rs.getString("course_id");
		rd.setCourse(params.findCourseById(courseId));
		
		String typeId = rs.getString("type_id");
		rd.setType(params.findReserveAttemptDateTypeById(typeId));
		
		String studentGroup = rs.getString("studentGroup");
		rd.setStudentGroup(studentGroup);
		
		String areaId = rs.getString("area_id");
		rd.setArea(params.findAreaById(areaId));
		
		rd.setCreationDate(rs.getTimestamp("created"));
		rd.setDescription(rs.getString("description"));		
		rd.setVersion(rs.getLong("version"));
		
		return rd;
	}	

	@Override
	public ReserveAttemptDate findById(String id) throws MapauException,PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from reserveattemptdate r where r.id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ReserveAttemptDate rad = getReserveAttemptDate(rs);
							loadCharacteristics(con, rad);
							loadPersons(con, rad);
							return rad;
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
	public List<ReserveAttemptDate> findBy(ReserveAttempt ra) throws MapauException, PersonException {
		throw new MapauException("Not Implemented");
	}

	@Override
	public List<ReserveAttemptDate> findBy(Date start, Date end) throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM reserveattemptdate ra_d WHERE ra_d.rstart >= ? and ra_d.rend <= ? and ra_d.related_id is NULL";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(start.getTime()));
					st.setTimestamp(2, new Timestamp(end.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<ReserveAttemptDate> dates = new ArrayList<ReserveAttemptDate>();
						while (rs.next()) {
							ReserveAttemptDate rad = getReserveAttemptDate(rs);
							loadCharacteristics(con, rad);
							loadPersons(con, rad);
							dates.add(rad);
						}
						return dates;
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
	public List<ReserveAttemptDate> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from reserveattemptdate r where (rstart > ? or rend > ?) AND (rstart < ? or rend < ?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					List<ReserveAttemptDate> reserves = new ArrayList<ReserveAttemptDate>();
					for (DatesRange rd : dates) {
						Date start = rd.getStart();
						Date end = rd.getEnd();
					
						st.setTimestamp(1, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(2, new java.sql.Timestamp(start.getTime()));
						st.setTimestamp(3, new java.sql.Timestamp(end.getTime()));
						st.setTimestamp(4, new java.sql.Timestamp(end.getTime()));
					
						ResultSet rs = st.executeQuery();
						try {
							while (rs.next()) {
								ReserveAttemptDate rad = getReserveAttemptDate(rs);
								loadCharacteristics(con, rad);
								loadPersons(con, rad);
								reserves.add(rad);
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
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}

	private void persistCharacteristics(Connection connection, ReserveAttemptDate rad) throws SQLException, MapauException {
		
		removeCharacteristics(connection, rad);
	   	
	   	if (rad.getCharacteristicsQuantity() != null) {
	   		List<CharacteristicQuantity> chars = rad.getCharacteristicsQuantity();
		   	
	   		String query = "INSERT INTO reserveattemptdate_characteristic (characteristic_id, reserveattemptdate_id, quantity) VALUES (?, ?, ?);" ;
		   	PreparedStatement st = connection.prepareStatement(query) ;
	   		
		   	try {
			   	for (CharacteristicQuantity c : chars) {
				   	st.setString(1, c.getCharacteristic().getId());
				   	st.setString(2, rad.getId());
				   	st.setLong(3, c.getQuantity());
				   	st.executeUpdate();
		   		}
	   		} finally {
	   			st.close();
	   		}
	   	}	
	}	
	
	private void persistPersons(Connection connection, ReserveAttemptDate rad) throws SQLException {
		
		removePersons(connection, rad);
	   	
	   	if (rad.getRelatedPersons() != null) {
	   		
	   		List<Person> persons = rad.getRelatedPersons();
	   		
		   	String query = "INSERT INTO reserveattemptdate_person (person_id, reserveattemptdate_id) VALUES (?, ?);" ;
		   	PreparedStatement st = connection.prepareStatement(query);
	   		
		   	try {
			   	for (Person p : persons) {
				   	st.setString(1, p.getId());
				   	st.setString(2, rad.getId());
				   	st.executeUpdate();
		   		}
		   	} finally {
		   		st.close();
		   	}
	   	}
	}	
	
	@Override
	public String persist(ReserveAttemptDate date) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (date.getId() == null) {
					String id = UUID.randomUUID().toString();
					date.setId(id);
					query = "insert into reserveattemptdate (rstart,rend,version,related_id, creator_id, created, description, course_id, type_id, studentGroup, area_id, id) values (?,?,?,?,?,?,?,?,?,?,?,?);";
				} else {
					query = "update reserveattemptdate set rstart = ?, rend = ?, version = ?, related_id = ?, creator_id = ?, created = ?, description = ?, course_id = ?, type_id = ?, studentGroup = ?, area_id = ? where id = ?";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(date.getStart().getTime()));
					st.setTimestamp(2, new Timestamp(date.getEnd().getTime()));
					st.setLong(3, date.getVersion());
					
					ReserveAttemptDate relatedDate = date.getRelated();
					String relatedId = null;
					if (relatedDate != null && relatedDate.getId() != null) {
						relatedId = relatedDate.getId();
					}
					st.setString(4,relatedId);
					
					Person creator = date.getCreator();
					String creatorId = creator.getId();
					st.setString(5, creatorId);
					
					st.setTimestamp(6, new Timestamp(date.getCreationDate().getTime()));				
					st.setString(7, date.getDescription());
					st.setString(8,date.getCourse().getId());
					st.setString(9,date.getType().getId());
					st.setString(10,date.getStudentGroup());
					st.setString(11,date.getArea().getId());
					st.setString(12,date.getId());
					
				   	st.executeUpdate();

				   	persistPersons(con, date);
				   	persistCharacteristics(con, date);
				   	
				   	return date.getId();					
					
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
	private void removePersons(Connection connection, ReserveAttemptDate rad) throws SQLException {
		String query = "delete from reserveattemptdate_person where reserveattemptdate_id = ?";
	   	PreparedStatement st = connection.prepareStatement(query);
	   	try {
		   	st.setString(1, rad.getId());
		   	st.executeUpdate();
	   	} finally {
	   		st.close();
	   	}
	}	
	
	@Override
	public void remove(ReserveAttemptDate date) throws MapauException {
		if (date == null) {
			throw new MapauException("reserveAttemptDate == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				removePersons(con, date);
				removeCharacteristics(con, date);
				
				String query = "delete from reserveattemptdate where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, date.getId());
					st.executeUpdate();
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
	

	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////// CHARACTERISTICS /////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	private CharacteristicQuantity findCharacteristic(Connection con, Characteristic chars, ReserveAttemptDate reserve) throws SQLException, MapauException {
		if (reserve == null || reserve.getId() == null) {
			throw new MapauException("reserve == null");
		}
		
		if (chars == null || chars.getId() == null) {
			throw new MapauException("characteristic == null");
		}
		
		
		String idReserve = reserve.getId();
		String idChars = chars.getId();
		
		String query = "select quantity from reserveattemptdate_characteristic where reserveattemptdate_id = ? and characteristic_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, idReserve);
			st.setString(2, idChars);
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					Long quantity = rs.getLong("quantity");
					CharacteristicQuantity cq = new CharacteristicQuantityBean();
					cq.setCharacteristic(chars);
					cq.setQuantity(quantity);
					return cq;
				} else {
					return null;
				}					
			} finally {
				rs.close();
			}			
		} finally {
			st.close();
		}
	}	
	
	@Override
	public void persist(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException {		
		try {
			Connection con = cp.getConnection();
			try {
				Characteristic chars = characteristicQuantity.getCharacteristic();
				CharacteristicQuantity cq = findCharacteristic(con, chars, reserveAttemptDate);
				String query;
				if (cq == null) {
					query = "INSERT INTO reserveattemptdate_characteristic (quantity,reserveattemptdate_id,characteristic_id) VALUES (?, ?, ?);";
				} else {
					query = "UPDATE reserveattemptdate_characteristic SET quantity = ? WHERE reserveattemptdate_id = ? and characteristic_id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					Long quantity = characteristicQuantity.getQuantity();
					st.setLong(1, quantity);
					st.setString(2, reserveAttemptDate.getId());
					st.setString(3, chars.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
			
		} catch (SQLException | NullPointerException e) {
			throw new MapauException(e);
		}
	}	

	private void removeCharacteristics(Connection connection, ReserveAttemptDate rad) throws SQLException {
		String query = "delete from reserveattemptdate_characteristic where reserveattemptdate_id = ?";
	   	PreparedStatement st = connection.prepareStatement(query);
	   	try {
	   		st.setString(1, rad.getId());
	   		st.executeUpdate();
	   	} finally {
	   		st.close();
	   	}
	}	
	
	@Override
	public void remove(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "delete from reserveattemptdate_characteristic where reserveattemptdate_id = ? and characteristic_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					Characteristic chars = characteristicQuantity.getCharacteristic();
					st.setString(1, reserveAttemptDate.getId());
					st.setString(2, chars.getId());
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
	
	@Override
	public void removeAllCharacteristics(ReserveAttemptDate reserveAttemptDate) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				removeCharacteristics(con, reserveAttemptDate);
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}	

}
