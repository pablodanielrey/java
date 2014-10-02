package ar.com.dcsys.data.person;

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

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.exceptions.PersonException;

public class PersonPostgreSqlDAO implements PersonDAO {

	private final static Logger logger = Logger.getLogger(PersonDAO.class.getName());
	
	private static final long serialVersionUID = 1L;
	private final PostgresSqlConnectionProvider cp;

	@Inject
	public PersonPostgreSqlDAO(PostgresSqlConnectionProvider cp) {
		this.cp = cp;
	}
	
	private interface QueryProcessor {
		public void setParams(PreparedStatement st) throws SQLException;
		public void process(ResultSet rs) throws SQLException, PersonException;
		public void postProcess(Connection con) throws SQLException;
	}

	private abstract class AbstractQueryProcessor implements QueryProcessor {
		@Override
		public void setParams(PreparedStatement st) throws SQLException {
		}
		
		@Override
		public abstract void process(ResultSet rs) throws SQLException, PersonException;
		
		@Override
		public void postProcess(Connection con) throws SQLException {
		}
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
			
			rp.postProcess(con);
			
		} finally {
			con.close();
		}
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
			PreparedStatement st = con.prepareStatement("create table if not exists persons (" +
														"id varchar not null primary key," +
														"name varchar not null," +
														"lastName varchar not null," +
														"dni varchar not null," +
														"address varchar," +
														"city varchar," +
														"country varchar," +
														"gender varchar)");
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persons_persontypes (" +
														"person_id varchar not null," +
														"persontype_id varchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persontypes (" + 
														"id varchar not null primary key," + 
														"type varchar not null");
			
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persontypestudent (" +
														"id varchar not null primary key," +
														"studentNumber varchar not null");
			
			try {
				st.execute();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists persons_mails (" +
					"person_id varchar not null," +
					"mail varchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}			
			
			st = con.prepareStatement("create table if not exists persons_telephones (" +
					"person_id varchar not null," +
					"telephone varchar not null)");
			try {
				st.execute();
			} finally {
				st.close();
			}			
		
		} finally {
			con.close();
		}
	}
	
	
	private Mail getMail(ResultSet rs) throws SQLException {
		Mail mail = new Mail();
		mail.setMail(rs.getString("mail"));
		mail.setPrimary(true);
		return mail;
	}
	
	@Override
	public void addMail(String personId, Mail mail) throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into persons_mails (person_id, mail) values (?,?)");
				try {
					st.setString(1, personId);
					st.setString(2, mail.getMail());
					st.execute();
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			throw new PersonException(e);
		}
	}
	
	@Override
	public List<Mail> findAllMails(String personId) throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from persons_mails where person_id = ?");
				try {
					st.setString(1, personId);
					ResultSet rs = st.executeQuery();
					try {
						List<Mail> mails = new ArrayList<>();
						while (rs.next()) {
							Mail m = getMail(rs);
							mails.add(m);
						}
						return mails;
						
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			throw new PersonException(e);
		}
	}
	
	
	@Override
	public void removeMail(String personId, Mail mail) throws PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("delete from persons_mails where person_id = ? and mail = ?");
				try {
					st.setString(1, personId);
					st.setString(2, mail.getMail());
					st.execute();
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			throw new PersonException(e);
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
				@Override
				public void postProcess(Connection con) throws SQLException {
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
				final String id = pt.toString();
				
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
					@Override
					public void postProcess(Connection con) throws SQLException {
					}
				});
			}
			
			return ids;
		
		} catch (SQLException e) {
			throw new PersonException(e);
		}
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
		person.setGender((gender != null) ? Gender.valueOf(gender) : null);
		
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
				@Override
				public void postProcess(Connection con) throws SQLException {
					for (Person p : persons) {
						loadTypes(con, p);						
					}
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
				@Override
				public void postProcess(Connection con) throws SQLException {
					if (persons[0] == null) {
						return;
					}
					loadTypes(con, persons[0]);				
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
				@Override
				public void postProcess(Connection con) throws SQLException {
					if (persons[0] == null) {
						return;
					}
					loadTypes(con, persons[0]);				
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
				
				String insertQuery = "insert into persons (dni,name,lastName,address,city,country,gender,id) values (?,?,?,?,?,?,?,?)"; 
				StringBuffer sb = new StringBuffer();
				if (p.getId() == null) {
					sb.append(insertQuery);
					String id = UUID.randomUUID().toString();
					p.setId(id);
				} else {
					
					String query = "select id from persons where id = ?";
					PreparedStatement st = con.prepareStatement(query);
					try {
						st.setString(1, p.getId());
						ResultSet rs = st.executeQuery();
						try {
							if (rs.next()) {
								sb.append("update persons set dni = ?, name = ?, lastName = ?, address = ?, city = ?, country = ?, gender = ? where id = ?");
							} else {
								sb.append(insertQuery);
							}
						} finally {
							rs.close();
						}
					} finally {
						st.close();
					}
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
					st.setString(7,(p.getGender() == null) ? null : p.getGender().toString());
					st.setString(8,p.getId());
					
					st.executeUpdate();
					
					persistTypes(con, p);
					
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

	
	/**
	 * Persiste los tipos de la persona indicada en el parametro.
	 * Esa persona ya debe tener un id cargado!!. o sea ya debe estar persistida.
	 * Deja la base exactametne con el contenido de p.getTypes();
	 * @param con
	 * @param p
	 * @throws SQLException
	 */
	private void persistTypes(Connection con, Person p) throws SQLException {
		
		deleteAllPersonTypes(con, p.getId());		
		
		
		if (p.getTypes() == null || p.getTypes().size() <= 0) {
			return;
		}
		
		String query = "insert into persons_persontypes (person_id, persontype_id) values (?,?)";
		PreparedStatement st = con.prepareStatement(query);
		try {
			for (PersonType pt : p.getTypes()) {
				st.setString(1,p.getId());
				st.setString(2,pt.toString());
				st.execute();
				persistPersonType(con,pt);
			};
		} finally {
			st.close();
		}
	}
	
	private void persistPersonType(Connection con, PersonType pt) throws SQLException {		
		if (pt.getId() == null) {
			String id = UUID.randomUUID().toString();
			pt.setId(id);
		}
		
		String query = "insert into persontypes (id, type) values (?,?)";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, pt.getId());
			st.setString(2, pt.getClass().getName());
			st.execute();
		} finally {
			st.close();
		}
		
		if (pt instanceof PersonTypeStudent) {
			query = "insert into persontypestudent (id,studentNumber) values (?,?)";
			st = con.prepareStatement(query);
			try {
				st.setString(1,pt.getId());
				st.setString(2,((PersonTypeStudent) pt).getStudentNumber());
				st.execute();
			} finally {
				st.close();
			}
		}
	}
	
	private void deleteAllPersonTypes(Connection con, String personId) throws SQLException {
		String query = "select persontype_id from persons_persontypes where person_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, personId);
			ResultSet rs = st.executeQuery();
			try {
				while (rs.next()) {
					String typeId = rs.getString("persontype_id");
					deletePersonType(con,typeId);					
				}
				
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
		
		query = "delete from persons_persontypes where person_id = ?";
		st = con.prepareStatement(query);
		try {
			st.setString(1, personId);
			st.execute();
		} finally {
			st.close();
		}
	}
	
	private void deletePersonType(Connection con, String typeId) throws SQLException {
		String query = "delete from persontypes where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, typeId);
			st.execute();
		} finally {
			st.close();
		}
		
		query = "delete from persontypestudent where id = ?";
		st = con.prepareStatement(query);
		try {
			st.setString(1, typeId);
			st.execute();			
		} finally {
			st.close();
		}
	}
	
	/**
	 * Carga los tipos de la persona indicada en el parámetro.
	 * @param con
	 * @param p
	 * @throws SQLException
	 */
	private void loadTypes(Connection con, Person p) throws SQLException {
		
		if (p.getTypes() == null) {
			p.setTypes(new ArrayList<PersonType>());
		}
		
		String query = "select persontype_id from persons_persontypes where person_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1,p.getId());
			ResultSet rs = st.executeQuery();
			try {
				while (rs.next()) {
					String typeId = rs.getString("persontype_id");					
					PersonType type = getPersonType(con, typeId);
					p.getTypes().add(type);
				}
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}
	
	private PersonType getPersonType(Connection con, String id) throws SQLException {
		String query = "select type from persontypes where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					String typeStr = rs.getString("type");
					return getPersonType(con,id,typeStr);
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
	
	private PersonType getPersonType(Connection con, String id, String type) throws SQLException {
		
		PersonType personType = null;
		
		if (PersonTypeStudent.class.getName().equals(type)) {
			personType = getPersonTypeStudent(con,id);
		}
		
		else if (PersonTypeExternal.class.getName().equals(type)) {
			personType = new PersonTypeExternal();
		}
		
		else if (PersonTypePostgraduate.class.getName().equals(type)) {
			personType = new PersonTypePostgraduate();
		}
		
		else if (PersonTypeTeacher.class.getName().equals(type)) {
			personType = new PersonTypeTeacher();
		}
		
		else if (PersonTypePersonal.class.getName().equals(type)) {
			personType = new PersonTypePersonal();
		}
		
		if (personType != null) {
			personType.setId(id);
		}
		
		return personType;
	}
	
	private PersonType getPersonTypeStudent(Connection con, String id) throws SQLException {
		String query = "select * from persontypestudent where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					PersonTypeStudent t = new PersonTypeStudent();
					t.setStudentNumber(rs.getString("studentNumber"));
					return t;
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
	
}
