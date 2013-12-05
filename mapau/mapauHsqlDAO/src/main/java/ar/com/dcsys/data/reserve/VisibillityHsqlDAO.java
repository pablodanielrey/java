package ar.com.dcsys.data.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.exceptions.MapauException;

public class VisibillityHsqlDAO implements VisibillityDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(VisibillityHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	protected final Params params;

	@Inject
	public VisibillityHsqlDAO(HsqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
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
			PreparedStatement st = con.prepareStatement("create table if not exists visibillity (" +
																"id longvarchar primary key not null," +
																"reserveAttemptDate_id longvarchar not null," +
																"type longvarchar not null )");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	protected String getVisibleType(Visible v) throws MapauException {
		if (v instanceof Visible) {
			return Type.VISIBLE.toString(); 
		}
		throw new MapauException("visible.type == desconocido");
	}	
	
	@Override
	public Visible findBy(ReserveAttemptDate date) throws MapauException {
		if (date == null) {
			throw new MapauException("date == null");
		}
		if (date.getId() == null) {
			throw new MapauException("date.getId == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id,reserveAttemptDate_id from visibillity where reserveAttemptDate_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, date.getId());
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Visible v = getVisible(rs);
							return v;
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
	public String persist(Visible visible) throws MapauException {
		ReserveAttemptDate date = visible.getDate();
		if (date == null || date.getId() == null) {
			throw new MapauException("visible.reserveAttemptDate == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (visible.getId() == null) {
					String id = UUID.randomUUID().toString();
					visible.setId(id);
					query = "insert into visibillity (reserveAttemptDate_id,type,id) values (?,?,?)";
				} else {
					query = "update visibillity set (reserveAttemptDate_id = ?,type = ?) where id = ?";
				} 
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,date.getId());
					st.setString(2,getVisibleType(visible));
					st.setString(3,visible.getId());
					st.executeUpdate();
					return visible.getId();					
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
	public void remove(Visible visible) throws MapauException {
		if (visible.getId() == null) {
			throw new MapauException("visible.id == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "delete from visibillity where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,visible.getId());
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

	protected Visible getNewVisible(Type type) throws MapauException {
		if (type == Type.VISIBLE) {
			return new VisibleBean();	
		}
		throw new MapauException(type.toString() + " desconocido");
	}
	
	protected Visible getVisible(ResultSet rs) throws MapauException, SQLException {
		String id = rs.getString("id");
		String type = rs.getString("type");
		String rid = rs.getString("reserveAttemptDate_id");
		ReserveAttemptDate rad = params.findReserveAttemptDateById(rid);

		Visible v = getNewVisible(Type.valueOf(type));
		v.setId(id);
		v.setDate(rad);
		
		return v;
	}	
	
}
