package ar.com.dcsys.data.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import ar.com.dcsys.data.person.types.PersonType;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.persistence.JdbcConnectionProvider;

@Named
public class PersonHsqlDAO extends  AbstractPersonDAO {

	private static final long serialVersionUID = 1L;
	private final JdbcConnectionProvider cp;

	@Inject
	public PersonHsqlDAO(JdbcConnectionProvider cp) {
		this.cp = cp;
	}
	
	private interface QueryProcessor {
		public void setParams(PreparedStatement st) throws SQLException;
		public void process(ResultSet rs) throws SQLException, PersonException;
	}

	private abstract class AbstractQueryProcessor implements QueryProcessor {
		@Override
		public void setParams(PreparedStatement st) throws SQLException {
		}
		
		@Override
		public abstract void process(ResultSet rs) throws SQLException, PersonException;
	}
	
	private void executeSqlQuery(String query, QueryProcessor rp) throws SQLException, PersonException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement(query);
			try {
				rp.setParams(st);
				ResultSet rs = st.executeQuery();
				try {
					while (rs.next()) {
						rp.process(rs);
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
	}
	
	
	@PostConstruct
	void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists persons (" +
														"id longvarchar not null primary key," +
														"name longvarchar not null," +
														"lastName longvarchar not null," +
														"dni longvarchar not null," +
														"address longvarchar," +
														"city longvarchar," +
														"country longvarchar," +
														"gender longvarchar)");
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persons_persontypes (" +
														"person_id longvarchar not null," +
														"persontype_id longvarchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persons_mails (" +
					"person_id longvarchar not null," +
					"mail longvarchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}			
			
			st = con.prepareStatement("create table if not exists persons_telephones (" +
					"person_id longvarchar not null," +
					"telephone longvarchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}			
		
		} finally {
			con.close();
		}
	}
	
	
	@Override
	public List<String> findAllIds() throws PersonException {
		
		try {
			final List<String> result = new ArrayList<>();
			
			executeSqlQuery("select id from persons", new AbstractQueryProcessor() {
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					String id = rs.getString("id");
					result.add(id);
				}
			});

			return result;
			
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public String findIdByDni(final String dni) throws PersonException {

		try {
			final String[] rid = new String[1]; rid[0] = null;
			
			executeSqlQuery("select id from persons where dni = ?", new QueryProcessor() {
				@Override
				public void setParams(PreparedStatement st) throws SQLException {
					st.setString(1, dni);
				}
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					String id = rs.getString("id");
					rid[0] = id;
				}
			});

			return rid[0];
			
		} catch (SQLException e) {
			throw new PersonException(e);
		}
		
	}

	@Override
	public List<String> findAllIdsBy(List<PersonType> type) throws PersonException {
		
		try {

			final List<String> ids = new ArrayList<>();
			
			for (PersonType pt : type) {
				final String id = pt.getId();
				
				executeSqlQuery("select person_id from persons_persontypes where persontype_id = ?", new QueryProcessor() {
					@Override
					public void setParams(PreparedStatement st)	throws SQLException {
						st.setString(1, id);
					}
					@Override
					public void process(ResultSet rs) throws SQLException, PersonException {
						String id = rs.getString("person_id");
						ids.add(id);
					}
				});
			}
			
			return ids;
		
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	
	private Mail getMail(ResultSet rs) throws SQLException {
		return null;
	}
	
	/**
	 * Retora una persona sacada de los datos del resultset.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Person getPerson(ResultSet rs) throws SQLException {
		
		String id = rs.getString("id");
		String dni = rs.getString("dni");
		String address = rs.getString("address");
		String name = rs.getString("name");
		String lastName = rs.getString("lastName");
		String city = rs.getString("city");
		String country = rs.getString("country");
		String gender = rs.getString("gender");
		
		Person person = new Person();
		person.setId(id);
		person.setDni(dni);
		person.setName(name);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setCity(city);
		person.setCountry(country);
		person.setGender(gender);
		
		return person;
	}
	
	
	@Override
	public List<Person> findAll() throws PersonException {
		try {

			final List<Person> persons = new ArrayList<>();
				
			executeSqlQuery("select * from persons", new AbstractQueryProcessor() {
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					Person person = getPerson(rs);
					persons.add(person);
				}
			});
			
			return persons;
		
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public List<Person> findAll(final Iterable<? extends String> ids)	throws PersonException {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from persons where id in (");
		for (String id : ids) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		String query = sb.toString();
		
		final List<Person> persons = new ArrayList<>();
		
		try {
			executeSqlQuery(query, new QueryProcessor() {
				@Override
				public void setParams(PreparedStatement st) throws SQLException {
					int i = 1;
					for (String id : ids) {
						st.setString(i, id);
						i++;
					}
				}
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					Person person = getPerson(rs);
					persons.add(person);
				}
			});
	
			return persons;
		
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public Person findById(final String id) throws PersonException {
		
		final Person[] persons = new Person[1]; persons[0] = null;
		
		try {
			executeSqlQuery("select * from persons where id = ?", new QueryProcessor() {
				@Override
				public void setParams(PreparedStatement st) throws SQLException {
					st.setString(1, id);
				}
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					Person person = getPerson(rs);
					persons[0] = person;
				}
			});
	
			return persons[0];
		
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public Person findByDni(final String dni) throws PersonException {
		final Person[] persons = new Person[1]; persons[0] = null;
		
		try {
			executeSqlQuery("select * from persons where dni = ?", new QueryProcessor() {
				@Override
				public void setParams(PreparedStatement st) throws SQLException {
					st.setString(1, dni);
				}
				@Override
				public void process(ResultSet rs) throws SQLException, PersonException {
					Person person = getPerson(rs);
					persons[0] = person;
				}
			});
	
			return persons[0];
			
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

	@Override
	public void remove(Person p) throws PersonException {
		throw new PersonException("not implemented");
	}

	@Override
	public String persist(Person p) throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				
				StringBuffer sb = new StringBuffer();
				if (p.getId() == null) {
					sb.append("insert into persons (dni,name,lastName,address,city,country,gender,id) values (?,?,?,?,?,?,?,?)");
					String id = UUID.randomUUID().toString();
					p.setId(id);
				} else {
					sb.append("update persons set dni = ?, name = ?, lastName = ?, address = ?, city = ?, country = ?, gender = ? where id = ?");
				}
				
				String query = sb.toString();
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,p.getDni());
					st.setString(2,p.getName());
					st.setString(3,p.getLastName());
					st.setString(4,p.getAddress());
					st.setString(5,p.getCity());
					st.setString(6,p.getCountry());
					st.setString(7,p.getGender());
					st.setString(8,p.getId());
					
					st.executeUpdate();
					
					return p.getId();
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new PersonException(e);
		}
	}

}
