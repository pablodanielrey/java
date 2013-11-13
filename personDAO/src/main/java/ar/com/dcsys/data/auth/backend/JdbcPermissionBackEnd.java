package ar.com.dcsys.data.auth.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.auth.Permission;
import ar.com.dcsys.group.entities.Group;
import ar.com.dcsys.persistence.JdbcConnectionProvider;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;

public class JdbcPermissionBackEnd implements PermissionBackEnd {

	private static final Logger logger = Logger.getLogger(JdbcPermissionBackEnd.class.getName());
	private final JdbcConnectionProvider cp;
	private final Params params;
	
	public JdbcPermissionBackEnd(JdbcConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@Override
	public void initialize() throws PersonException {
		createTables();
	}

	private void createTables() throws PersonException {
		
		try {
			Connection con = cp.getConnection();

			try {
				PreparedStatement st = con.prepareStatement("create table if not exists permissions (" +
						"id varchar not null primary key," +
						"person_id varchar not null," +
						"node_id varchar not null," +
						"role varchar not null" +
						")");
				st.executeUpdate();
				st.close();
				
			} finally {
				con.close();
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
	}
		
	@Override
	public void destroy() {
	}

	
	@Override
	public List<Permission> findAll() throws PersonException {
		List<Permission> permissions = new ArrayList<Permission>();
		
		try {
			
			Connection con = cp.getConnection();
			try {
				
				String query = "select id, person_id, node_id, role from permissions";
				PreparedStatement st = con.prepareStatement(query);
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					Permission p = loadPermission(rs);
					permissions.add(p);
				}
				
				
			} finally {
				con.close();
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
		
		return permissions;
	}
	
	
	private Permission loadPermission(ResultSet rs) throws SQLException, PersonException {
		String id = rs.getString("id");
		String person_id = rs.getString("person_id");
		String node_id = rs.getString("node_id");
		String role = rs.getString("role");
		
		Group node = params.findNodeById(node_id);
		Person person = params.findPersonById(person_id);
				
		Permission p = new Permission();
		p.setNode(node);
		p.setProfile(role);
		p.setPerson(person);
		
		return p;
	}
	
	@Override
	public List<Permission> findBy(Person person) throws PersonException {
		
		if (person == null) {
			throw new PersonException("person == null");
		}
		
		String personId = person.getId();
		if (personId == null) {
			throw new PersonException("person.id == null");
		}
		
		List<Permission> permissions = new ArrayList<Permission>();
		
		try {
			
			Connection con = cp.getConnection();
			try {
				
				String query = "select id, person_id, node_id, role from permissions where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, personId);
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					Permission p = loadPermission(rs);
					permissions.add(p);
				}
				
				
			} finally {
				con.close();
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
		
		return permissions;
	}

	@Override
	public boolean hasPermissions(Person person) throws PersonException {
		if (person == null) {
			throw new PersonException("person == null");
		}
		
		String personId = person.getId();
		if (personId == null) {
			throw new PersonException("person.id == null");
		}
		
		try {
			
			Connection con = cp.getConnection();
			try {
				
				String query = "select true from permissions where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, personId);
				ResultSet rs = st.executeQuery();
				return (rs.next());
				
			} finally {
				con.close();
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
		
	}	
	
	
}
