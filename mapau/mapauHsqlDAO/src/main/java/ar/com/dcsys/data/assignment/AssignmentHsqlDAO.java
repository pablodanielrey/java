package ar.com.dcsys.data.assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.AssignableUnit;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class AssignmentHsqlDAO implements AssignmentDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(AssignmentHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public AssignmentHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists assignment (" +
															   	"id longvarchar NOT NULL PRIMARY KEY," +
													   			"notes longvarchar," +
															   	"from_ DATE," +
													   			"to_ DATE," +
															   	"type longvarchar," +
															   	"person_id longvarchar NOT NULL," +
													   			"assignableunit_id longvarchar NOT NULL ," +
													   			"version BIGINT NULL);");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists assignment_relatedassignment (" +
									   			"assignment_id longvarchar NOT NULL ,"+
									   			"relatedassignment_id longvarchar NOT NULL);");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
   			st = con.prepareStatement("CREATE TABLE IF NOT EXISTS assignment_legaldocument (" +
								   			"assignment_id longvarchar NOT NULL ,"+
								   			"legaldocument_id longvarchar NOT NULL);");
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
	 * Carga el Assignment desde el ResultSet
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws PersonException
	 * @throws MapauException
	 */
	
	private Assignment getAssignment(ResultSet resultSet) throws SQLException, PersonException, MapauException {
   		String id = resultSet.getString("id");
   		Date from = resultSet.getDate("from_");
   		Date to = resultSet.getDate("to_");   		
   		String notes = resultSet.getString("notes");
   		String type = resultSet.getString("type");
   		Long version = resultSet.getLong("version");
   		Person person = params.findPersonById(resultSet.getString("person_id"));
   		AssignableUnit assignableUnit = params.findAssignableUnitById(resultSet.getString("assignableunit_id"));
   		
		Assignment assignment = new AssignmentBean();
   		assignment.setId(id);
   		assignment.setFrom(from);
   		assignment.setTo(to);
   		assignment.setNotes(notes);
   		assignment.setType(type);
   		assignment.setVersion(version);	   		   		
   		assignment.setPerson(person);	
   		assignment.setAssignableUnit(assignableUnit);
   	
   		return assignment;		
	}

	@Override
	public Assignment findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignment WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Assignment assig = getAssignment(rs);
							return assig;
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
		} catch (SQLException | PersonException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<String> findAllIds() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT id FROM assignment";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> ids = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							ids.add(id);
						} 
						return ids;
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
	public List<Assignment> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignment";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Assignment> assignments = new ArrayList<Assignment>();
						while (rs.next()) {
							Assignment assig = getAssignment(rs);
							assignments.add(assig);
						} 
						return assignments;
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

	@Override
	public List<String> findIdsBy(Person person) throws MapauException {
		if (person == null) {
			throw new MapauException("person == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT id FROM assignment where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, person.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<String> ids = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							ids.add(id);
						} 
						return ids;
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
	public List<Assignment> findBy(Person person) throws MapauException {
		if (person == null) {
			throw new MapauException("person == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignment where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, person.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<Assignment> assignments = new ArrayList<Assignment>();
						while (rs.next()) {
							Assignment assig = getAssignment(rs);
							assignments.add(assig);
						} 
						return assignments;
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

	@Override
	public List<String> findIdsBy(AssignableUnit assignableUnit) throws MapauException {
		if (assignableUnit == null) {
			throw new MapauException("assignableUnit == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT assignment.id FROM assignment INNER JOIN assignableunit ON (assignment.assignableunit_id = assignableunit.id) WHERE assignableunit_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, assignableUnit.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<String> ids = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							ids.add(id);
						} 
						return ids;
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
	public List<Assignment> findBy(AssignableUnit assignableUnit) throws MapauException {
		if (assignableUnit == null) {
			throw new MapauException("assignableUnit == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignment INNER JOIN assignableunit ON (assignment.assignableunit_id = assignableunit.id) WHERE assignableunit_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, assignableUnit.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<Assignment> assignmetnts = new ArrayList<Assignment>();
						while (rs.next()) {
							Assignment assig = getAssignment(rs);
							assignmetnts.add(assig);
						} 
						return assignmetnts;
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

	@Override
	public List<Assignment> findBy(AssignableUnit assignableUnit, String type) throws MapauException {
		if (assignableUnit == null) {
			throw new MapauException("assignableUnit == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM assignment INNER JOIN assignableunit ON (assignment.assignableunit_id = assignableunit.id) WHERE assignableunit_id = ? AND assignment.type = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, assignableUnit.getId());
					st.setString(2, type);
					ResultSet rs = st.executeQuery();
					try {
						List<Assignment> assignmetnts = new ArrayList<Assignment>();
						while (rs.next()) {
							Assignment assig = getAssignment(rs);
							assignmetnts.add(assig);
						} 
						return assignmetnts;
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

	@Override
	public String persist(Assignment assignment) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (assignment.getId() == null) {
					String id = UUID.randomUUID().toString();
					assignment.setId(id);
					query ="INSERT INTO assignment (notes, type, version, assignableunit_id, person_id, id) VALUES (?, ?, ?, ?, ?, ?);";
				} else {
					query ="UPDATE assignment SET notes = ?, type = ?, version = ?, assignableunit_id = ?, person_id = ? WHERE assignment.id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, assignment.getNotes());
					st.setString(2, assignment.getType());
					st.setLong(3, assignment.getVersion());
					st.setString(4, assignment.getAssignableUnit().getId());
					st.setString(5, assignment.getPerson().getId());
					st.setString(6, assignment.getId());
					
					st.executeUpdate();
					
					return assignment.getId();
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
	public void remove(Assignment assignment) throws MapauException {
		throw new MapauException("Not Implemented");
	}

	
}
