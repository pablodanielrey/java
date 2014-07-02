package ar.com.dcsys.data.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.persistence.JdbcConnectionProvider;


public class GroupSqlDAO implements GroupDAO {

	private final static Logger logger = Logger.getLogger(GroupSqlDAO.class.getName());
	private final JdbcConnectionProvider cp;
	
	@Inject
	public GroupSqlDAO(JdbcConnectionProvider cp) {
		this.cp = cp;
	}

	@PostConstruct
	private final void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				con.setAutoCommit(false);
				
				PreparedStatement st = con.prepareStatement("create table if not exists groups ("
						+ "id varchar not null primary key,"
						+ "name varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}

				st = con.prepareStatement("create table if not exists groups_persons ("
						+ "person_id varchar not null,"
						+ "group_id varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				st = con.prepareStatement("create table if not exists groups_mails ("
						+ "group_id varchar not null primary key,"
						+ "mail varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				st = con.prepareStatement("create table if not exists groups_types ("
						+ "group_id varchar not null primary key,"
						+ "type varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
			
				con.commit();
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	

	private Group getGroup(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String name = rs.getString("name");
		
		Group g = new Group();
		g.setId(id);
		g.setName(name);
		g.setTypes(new ArrayList<GroupType>());
		
		return g;
	}
	
	private void loadTypes(Connection con, Group g) throws SQLException {
		
		PreparedStatement st = con.prepareStatement("select * from groups_types where group_id = ?");
		try {
			st.setString(1, g.getId());
			ResultSet rs = st.executeQuery();
			try {
				g.getTypes().clear();
				while (rs.next()) {
					String type = rs.getString("type");
					GroupType t = GroupType.valueOf(type);
					g.getTypes().add(t);
				}
				
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}
	
	@Override
	public List<GroupType> findAllTypes() {
		return Arrays.asList(GroupType.values());
	}

	@Override
	public List<Group> findAll() throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from groups");
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Group> groups = new ArrayList<>();
						while (rs.next()) {
							Group g = getGroup(rs);
							loadTypes(con, g);
							groups.add(g);
						}
						return groups;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public List<String> findAllIds() throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select id from groups");
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> groups = new ArrayList<>();
						while (rs.next()) {
							String id = rs.getString("id");
							groups.add(id);
						}
						return groups;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public void loadMembers(Group g) throws PersonException {
		throw new PersonException("no implementado");
	}

	@Override
	public Group findById(String id) throws PersonException {
		if (id == null) {
			throw new PersonException("group.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from groups where id = ?");
				try {
					st.setString(1, id);
					
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Group g = getGroup(rs);
							loadTypes(con, g);
							return g;
						}
						return null;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}	
	}

	@Override
	public List<String> findByPerson(Person p) throws PersonException {
		if (p == null || p.getId() == null) {
			throw new PersonException("person == null || person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select group_id from groups_persons where person_id = ?");
				try {
					st.setString(1, p.getId());
					
					ResultSet rs = st.executeQuery();
					try {
						List<String> groups = new ArrayList<>();
						while (rs.next()) {
							String id = rs.getString("group_id");
							groups.add(id);
						}
						return groups;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}	
	}

	@Override
	public List<String> findByType(GroupType type) throws PersonException {
		
		if (type == null) {
			throw new PersonException("type == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select group_id from groups_types where type = ?");
				try {
					st.setString(1, type.toString());
					
					ResultSet rs = st.executeQuery();
					try {
						List<String> groups = new ArrayList<>();
						while (rs.next()) {
							String id = rs.getString("group_id");
							groups.add(id);
						}
						return groups;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}	
	}

	@Override
	public void addPersonToGroup(Group g, Person p) throws PersonException {
		if (g == null || g.getId() == null) {
			throw new PersonException("group == null || group.id == null");
		}
		
		if (p == null || p.getId() == null) {
			throw new PersonException("person == null || person.id == null");
		}
		
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into groups_persons (group_id, person_id) values (?,?)");
				try {
					st.setString(1, g.getId());
					st.setString(2, p.getId());
					st.executeUpdate();

				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}	
	}

	@Override
	public void removePersonFromGroup(Group g, Person p) throws PersonException {

		if (g == null || g.getId() == null) {
			throw new PersonException("group == null || group.id == null");
		}
		
		if (p == null || p.getId() == null) {
			throw new PersonException("person == null || person.id == null");
		}
		
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("delete from groups_persons where group_id = ? and person_id = ?");
				try {
					st.setString(1, g.getId());
					st.setString(2, p.getId());
					st.executeUpdate();

				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}	
	}

	@Override
	public String persist(Group g) throws PersonException {
		if (g == null) {
			throw new PersonException("group == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				con.setAutoCommit(false);
				
				String query = "";
				if (g.getId() == null) {
					String id = UUID.randomUUID().toString();
					g.setId(id);
					query = "insert into groups (name, group_id) values (?,?)";
				} else {
					query = "update groups set name = ? where group_id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, g.getId());
					st.setString(2, g.getName());
					st.executeUpdate();
					
				} finally {
					st.close();
				}
				
				List<GroupType> types = g.getTypes();
				if (types != null) {
					for (GroupType t : types) {
						query = "insert into groups_types (group_id, type) values (?,?)";
						st = con.prepareStatement(query);
						try {
							st.setString(1, g.getId());
							st.setString(2, t.toString());
							st.executeUpdate();
						} finally {
							st.close();
						}
					}
				}

				con.commit();
				
				return g.getId();
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}		
	}

	@Override
	public void remove(Group g) throws PersonException {
		if (g == null || g.getId() == null) {
			throw new PersonException("group == null || group.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				con.setAutoCommit(false);
				
				String query = "delete from groups where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, g.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
				
				query = "delete from groups_types where group_id = ?";
				st = con.prepareStatement(query);
				try {
					st.setString(1, g.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}

				query = "delete from groups_mails where group_id = ?";
				st = con.prepareStatement(query);
				try {
					st.setString(1, g.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
				
				query = "delete from groups_persons where group_id = ?";
				st = con.prepareStatement(query);
				try {
					st.setString(1, g.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
				
				con.commit();
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}		
		
	}

	@Override
	public String findParent(Group group) throws PersonException {
		throw new PersonException("no implementado");
	}

	@Override
	public List<String> findAllParents(List<Group> groups) throws PersonException {
		throw new PersonException("no implementado");
	}

	@Override
	public List<String> findAllSons(List<Group> groups) throws PersonException {
		throw new PersonException("no implementado");
	}

	@Override
	public void setParent(Group son, Group parent) throws PersonException {
		throw new PersonException("no implementado");
	}

}
