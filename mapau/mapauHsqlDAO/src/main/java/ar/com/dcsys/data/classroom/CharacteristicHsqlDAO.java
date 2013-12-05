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

public class CharacteristicHsqlDAO implements CharacteristicDAO {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(CharacteristicHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	
	@Inject
	public CharacteristicHsqlDAO(HsqlConnectionProvider cp) {
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(),e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists characteristic (" +
																"id longvarchar not null primary key, " +
																"name longvarchar not null," +
																"deleted boolean," +
																"version bigint not null)");	
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
	 * Carga la caracteristica del resultset
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private Characteristic getCharacteristic(ResultSet resultSet) throws SQLException {
   		String id = resultSet.getString("id");
   		String name = resultSet.getString("name");
   		Boolean deleted = resultSet.getBoolean("deleted");
   		Long version = resultSet.getLong("version");
				
   		Characteristic characteristic = new CharacteristicBean();
   		characteristic.setId(id);
   		characteristic.setName(name);
   		characteristic.setDeleted(deleted);
   		characteristic.setVersion(version);
   		return characteristic;
	}	
	

	@Override
	public Characteristic findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from characteristic where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Characteristic c = getCharacteristic(rs);
							return c;
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
				String query = "select id from characteristic where name = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, name);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							String id = rs.getString("id");
							return id;
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
	public Characteristic findByName(String name) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from characteristic where name = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, name);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Characteristic c = getCharacteristic(rs);
							return c;
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
	public List<Characteristic> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from characteristic";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Characteristic> characteristics = new ArrayList<Characteristic>();
						while (rs.next()) {
							Characteristic c = getCharacteristic(rs);
							characteristics.add(c);
						}
						return characteristics;
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
	public List<Characteristic> findAll(Boolean historic) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from characteristic WHERE deleted = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setBoolean(1, historic);
					ResultSet rs = st.executeQuery();
					try {
						List<Characteristic> characteristics = new ArrayList<Characteristic>();
						while (rs.next()) {
							Characteristic c = getCharacteristic(rs);
							characteristics.add(c);
						}
						return characteristics;
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
				String query = "select id from characteristic";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> characteristicsIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							characteristicsIds.add(id);
						}
						return characteristicsIds;
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
	public String persist(Characteristic characteristic) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (characteristic.getId() == null) {
					String id = UUID.randomUUID().toString();
					characteristic.setId(id);
					query = "INSERT INTO characteristic (name, deleted, version, id) VALUES (?, ?, ?, ?);";
				} else {
					query = "UPDATE characteristic SET name = ?, deleted = ?, version = ? WHERE characteristic.id = ?;";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, characteristic.getName());
				   	st.setBoolean(2, characteristic.getDeleted());
				   	st.setLong(3, characteristic.getVersion());
				   	st.setString(4, characteristic.getId());
				   	
				   	st.executeUpdate();
				   	
					return characteristic.getId();
					
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
	public void remove(Characteristic object) throws MapauException {
		throw new MapauException("Not Implemented");
	}	
}
