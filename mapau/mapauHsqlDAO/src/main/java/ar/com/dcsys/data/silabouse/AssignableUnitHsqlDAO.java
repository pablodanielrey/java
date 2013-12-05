package ar.com.dcsys.data.silabouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.exceptions.MapauException;

public class AssignableUnitHsqlDAO implements AssignableUnitDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(AssignableUnitHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public AssignableUnitHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists assignableunit (" +
																	"id longvarchar not null primary key, " +
																	"type longvarchar not null);");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	private AssignableUnit getAssignableUnit(ResultSet rs) throws MapauException, SQLException {
		String type = rs.getString("type");
		if (type.toLowerCase().contains("course")) {
			Course assignableUnit = params.findCourseById(rs.getString("id"));	   		
			return assignableUnit;
		} else {
			return null;
		}
	}

	@Override
	public AssignableUnit findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignableUnit c WHERE c.id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							return getAssignableUnit(rs);
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
	
	/**
	 * Retorno el tipo que posee au
	 */
	@Override
	public String findType(AssignableUnit au) throws MapauException {
		if (au == null) {
			throw new MapauException("au == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT type FROM assignableUnit c WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, au.getId());
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							return rs.getString("type");
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
	
	/**
	 * Precondiciones: au debe tener id, ya que primero se debio hacer persist en la clase correspondiente
	 */
	@Override
	public String persist(AssignableUnit au) throws MapauException {
		if (au == null) {
			throw new MapauException("au == null");
		}
		if (au.getId() == null) {
			throw new MapauException("au.id == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				/*
				 * busco si existe el au
				 */
				AssignableUnit aux = findById(au.getId());
				if (aux == null) {
					query = "INSERT INTO assignableunit (type, id) VALUES (?,?)";
				} else {
					query = "UPDATE assignableunit SET type = ? WHERE id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, au.getType());
					st.setString(2, au.getId());
					st.executeUpdate();
					
					return au.getId();
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
