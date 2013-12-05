package ar.com.dcsys.data.classroom;

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

public class ClassRoomHsqlDAO implements ClassRoomDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(ClassRoomHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public ClassRoomHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists classroom (" +
																"id longvarchar not null primary key, " +
																"name longvarchar not null," +
																"deleted boolean," +
																"version bigint not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists classroom_characteristic (" +
												"characteristic_id longvarchar NOT NULL ," +
												"quantity bigint not null," +
												"classroom_id longvarchar NOT NULL REFERENCES classroom (id ))");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}		
	}
	
	/**
	 * Carga todas las características del aula.
	 * @param classRoom
	 * @param connection
	 * @throws SQLException
	 * @throws MapauException
	 */
	private void loadAllCharacteristics(ClassRoom classRoom, Connection connection) throws SQLException,MapauException  {
	  	PreparedStatement st = connection.prepareStatement("SELECT characteristic_id, quantity FROM classroom_characteristic c_cq WHERE c_cq.classroom_id = ?");
	   	try {
		  	st.setString(1, classRoom.getId());
	   		ResultSet rs = st.executeQuery();
			try {
			   	while (rs.next()) {
					Long quantity = rs.getLong("quantity");
					String id = rs.getString("characteristic_id");
					
					CharacteristicQuantity c = new CharacteristicQuantityBean();
					c.setQuantity(quantity);
					c.setCharacteristic(params.findCharacteristicById(id));
					
					classRoom.getCharacteristicQuantity().add(c);
			   	}			   	
			} finally {
				rs.close();
			}
	   	} finally {
	   		st.close();
	   	}
	}
	
	/**
	 * Carga el aula desde el ResultSet.
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private ClassRoom getClassRoom(ResultSet resultSet) throws SQLException {
		
		String id = resultSet.getString("id");
		String name = resultSet.getString("name");
		Boolean deleted = resultSet.getBoolean("deleted");
		Long version = resultSet.getLong("version");
		
   		ClassRoom classRoom = new ClassRoomBean();
   		classRoom.setId(id);
   		classRoom.setName(name);
   		classRoom.setDeleted(deleted);
   		classRoom.setVersion(version);
   		
   		return classRoom;
	}	
	
	@Override
	public ClassRoom findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from classroom where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ClassRoom cr = getClassRoom(rs);
							loadAllCharacteristics(cr, con);
							return cr;
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
	public String findIdByName(String name) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from classroom where name = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, name);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							return rs.getString("id");
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
	public ClassRoom findByName(String name) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from classroom where name = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, name);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ClassRoom cr = getClassRoom(rs);
							loadAllCharacteristics(cr, con);
							return cr;
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
	public List<ClassRoom> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from classroom";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<ClassRoom> classrooms = new ArrayList<ClassRoom>();
						while (rs.next()) {
							ClassRoom cr = getClassRoom(rs);
							loadAllCharacteristics(cr, con);
							classrooms.add(cr);
						}
						return classrooms;
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
	public List<String> findAllIds() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from classroom";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> classroomsIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							classroomsIds.add(id);
						}
						return classroomsIds;
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
	public String persist(ClassRoom classroom) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (classroom.getId() == null) {
					String id = UUID.randomUUID().toString();
					classroom.setId(id);
					query = "INSERT INTO classroom (name, deleted, version, id) VALUES (?, ?, ?, ?);";
				} else {
					query = "UPDATE classroom SET name = ?, deleted = ?, version = ? WHERE classroom.id = ?;";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
				 	st.setString(1, classroom.getName());
				   	st.setBoolean(2, classroom.getDeleted());
				   	st.setLong(3, classroom.getVersion());
				   	st.setString(4, classroom.getId());
				   	
				   	st.executeUpdate();
				   	
				   	persistCharacteristics(con,classroom);
				   	
					return classroom.getId();
					
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
	 * Persiste la lista de cantidades de características.
	 * @param connection
	 * @param id
	 * @param chars
	 * @throws SQLException
	 */
	private void persistCharacteristics(Connection connection, ClassRoom classRoom) throws SQLException,MapauException  {
		
		removeCharacteristics(connection, classRoom);
		
		List<CharacteristicQuantity> chars = classRoom.getCharacteristicQuantity();

		String query = "INSERT INTO classroom_characteristic (characteristic_id, quantity, classroom_id) VALUES (?, ?, ?);" ;
	   	PreparedStatement st = connection.prepareStatement(query) ;
	   	try {	   	
		   	for (CharacteristicQuantity c : chars) {
			   	st.setString(1, c.getCharacteristic().getId());
			   	st.setLong(2, c.getQuantity());
			   	st.setString(3,classRoom.getId());
			   	st.executeUpdate();	   		
		   	}
	   	} finally {
	   		st.close();
	   	}
	}	
	
	private void removeCharacteristics(Connection connection, ClassRoom classRoom) throws SQLException {
		String query = "delete from classroom_characteristic where classroom_id = ?";
	   	PreparedStatement st = connection.prepareStatement(query);
	   	try {
		   	st.setString(1, classRoom.getId());
		   	st.executeUpdate();
	   	} finally {
	   		st.close();
	   	}
	}	

	@Override
	public void remove(ClassRoom classroom) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				removeCharacteristics(con, classroom);
				
				String query = "delete from classroom where classrooom_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, classroom.getId());
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
