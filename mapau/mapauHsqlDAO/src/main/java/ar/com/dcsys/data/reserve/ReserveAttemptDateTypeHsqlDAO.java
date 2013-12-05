package ar.com.dcsys.data.reserve;

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

public class ReserveAttemptDateTypeHsqlDAO implements ReserveAttemptDateTypeDAO {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ReserveAttemptDateTypeHsqlDAO.class.getName());
	
	private final HsqlConnectionProvider cp;
	
	@Inject
	public ReserveAttemptDateTypeHsqlDAO(HsqlConnectionProvider cp) {
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		try {
			createTables();
		} catch(SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists reserveattempttype (" +
																"id longvarchar not null primary key, " +
																"name longvarchar not null," +
																"description longvarchar," +
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

	private ReserveAttemptDateType getReserveAttemptType(ResultSet resultSet) throws SQLException {
   		String id = resultSet.getString("id");
   		String name = resultSet.getString("name");
   		String description = resultSet.getString("description");
   		Long version = resultSet.getLong("version");
		
		ReserveAttemptDateType reserveAttemptType = new ReserveAttemptDateTypeBean();
   		reserveAttemptType.setId(id);
   		reserveAttemptType.setName(name);
   		reserveAttemptType.setDescription(description);
   		reserveAttemptType.setVersion(version);
   		return reserveAttemptType;
	}
	
	@Override
	public ReserveAttemptDateType findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM ReserveAttemptType WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ReserveAttemptDateType type = getReserveAttemptType(rs);
							return type;
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
	public List<ReserveAttemptDateType> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM ReserveAttemptType";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<ReserveAttemptDateType> types = new ArrayList<ReserveAttemptDateType>();
						while (rs.next()) {
							ReserveAttemptDateType type = getReserveAttemptType(rs);
							types.add(type);
						}
						return types;
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
	public ReserveAttemptDateType findByName(String name) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM ReserveAttemptType WHERE name = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, name);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							ReserveAttemptDateType type = getReserveAttemptType(rs);
							return type;
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
	public List<String> findAllIds() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT id FROM ReserveAttemptType";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> typesIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							typesIds.add(id);
						}
						return typesIds;
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
	public String persist(ReserveAttemptDateType type) throws MapauException {
		if (type == null) {
			throw new MapauException("type == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				
				if (type.getId() == null) {
					String id = UUID.randomUUID().toString();
					type.setId(id);
					query = "INSERT INTO reserveattempttype (name, description, version, id) VALUES (?, ?, ?, ?);";
				} else {
					query = "UPDATE reserveAttemptType SET name = ?, description = ?, version = ? WHERE reserveAttemptType.id = ?;";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, type.getName());
				   	st.setString(2, type.getDescription());
				   	st.setLong(3, type.getVersion());
				   	st.setString(4, type.getId());
				   	
				   	st.executeUpdate();	
				   	
				   	return type.getId();
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
	public void remove(ReserveAttemptDateType object) throws MapauException {
		throw new MapauException("Not Implemented");
	}

}
