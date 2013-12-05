package ar.com.dcsys.data.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class VisibillityGroupHsqlDAO extends VisibillityHsqlDAO {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(VisibillityGroupHsqlDAO.class.getName());
	
	private final HsqlConnectionProvider cp;
	
	@Inject
	public VisibillityGroupHsqlDAO(HsqlConnectionProvider cp, Params params) {
		super(cp,params);
		this.cp = cp;
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
			PreparedStatement st = con.prepareStatement("create table if not exists visibillity_group (" +
																"id longvarchar primary key not null," +
																"group_id longvarchar not null )");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	@Override
	public String persist(Visible visible) throws MapauException {
		
		if (visible instanceof Visible) {
			
			return super.persist(visible);
			
		} else if (visible instanceof GroupVisible) {
			
			GroupVisible gvisible = (GroupVisible)visible;
			List<Group> groups = gvisible.getGroups();
			if (groups == null || groups.size() <= 0) {
				throw new MapauException("groupVisible.groups == null");
			}
	
			super.persist(gvisible);
			
			try {
				Connection con = cp.getConnection();
				try {
					
					remove(con,gvisible.getId());
					
					String query = "insert into visibillity_group (id,group_id) values (?,?)";
					PreparedStatement st = con.prepareStatement(query);					
					try {
						for (Group g : groups) {
							st.setString(1,gvisible.getId());
							st.setString(2,g.getId());
							st.executeUpdate();
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
		
		throw new MapauException("No se puede manejar ese tipo de visibilidad");
	}
	
	private void remove(Connection con, String gvisibleId) throws SQLException {
		String query = "delete from visibillity_group where id = ?";
		PreparedStatement st = con.prepareStatement(query);	
		try {
			st.setString(1, gvisibleId);
			st.executeUpdate();
		} finally {
			st.close();
		}		
	}
	
	@Override
	public void remove(Visible visible) throws MapauException {
		if (visible == null) {
			throw new MapauException("visible == null");
		}
		if (visible.getId() == null) {
			throw new MapauException("visible.id == null");
		}		
		super.remove(visible);
		
		try {
			Connection con = cp.getConnection();
			try {
				remove(con, visible.getId());
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	protected Visible getNewVisible(Type type) throws MapauException {
		if (type == Type.GROUPVISIBLE) {
			return new GroupVisibleBean(params.getGroupParams());
		}
		return super.getNewVisible(type);
	}
	
	
	@Override
	protected String getVisibleType(Visible v) throws MapauException {
		if (v instanceof GroupVisible) {
			return Type.GROUPVISIBLE.toString();
		}
		return super.getVisibleType(v);
	}
	
	protected void loadGroupVisibleData(GroupVisible v, ResultSet rs) throws PersonException, MapauException, SQLException {
		String gid = rs.getString("group_id");
		Group group = params.findGroupById(gid);
		v.getGroups().add(group);
	}	
	
	@Override
	public Visible findBy(ReserveAttemptDate date) throws MapauException {
		Visible v = super.findBy(date);
		if (!(v instanceof GroupVisible)) {
			return v;
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id,group_id from visibillity_group where id = ?";
				PreparedStatement st = con.prepareStatement(query);				
				try {
					st.setString(1, v.getId());
					ResultSet rs = st.executeQuery();
					try {
						while (rs.next()) {
							loadGroupVisibleData((GroupVisible)v, rs);
						}
						return v;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch (SQLException | PersonException e) {
			throw new MapauException(e);
		}
		
	}
	

}
