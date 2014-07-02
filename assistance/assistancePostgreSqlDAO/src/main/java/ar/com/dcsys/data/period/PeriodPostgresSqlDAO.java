package ar.com.dcsys.data.period;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;


public class PeriodPostgresSqlDAO implements PeriodDAO {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(PeriodPostgresSqlDAO.class.getName());
	private final PostgresSqlConnectionProvider cp;
	
	private final Params params;
	
	@Inject
	public PeriodPostgresSqlDAO(PostgresSqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@PostConstruct
	void init() {
		createTables();
	}
	
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists periodAssignation (" +
																	"id varchar not null primary key, " +
																	"person_id varchar not null, " +
																	"pstart timestamp not null, " +
																	"type varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private PeriodAssignation getPeriodAssignation(ResultSet rs) throws SQLException {
		PeriodAssignation pa = new PeriodAssignation();
		pa.setId(rs.getString("id"));
		pa.setStart(new Date(rs.getTimestamp("pstart").getTime()));
		pa.setType(PeriodType.valueOf(rs.getString("type")));
		return pa;
	}
	
	private void loadPerson(ResultSet rs, PeriodAssignation pa) throws SQLException, PersonException {
		String person_id = rs.getString("person_id");
		pa.setPerson(params.findPersonById(person_id));
	}


	@Override
	public PeriodAssignation findBy(Person person, Date date, PeriodType type) throws PeriodException {
		if (person.getId() == null) {
			throw new PeriodException("Person.id == null");
		}
		
		if (date == null) {
			throw new PeriodException("date == null");
		}
		
		if (type == null) {
			throw new PeriodException("PeriodType == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from periodAssignation where person_id = ? and pstart = ? and type = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,person.getId());
					st.setTimestamp(2,new Timestamp(date.getTime()));
					st.setString(3,type.toString());
					
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							PeriodAssignation pa = getPeriodAssignation(rs);
							pa.setPerson(person);
							return pa;
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
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PeriodException(e);
		}
	}	

	@Override
	public List<PeriodAssignation> findAllPeriodAssignations() throws PeriodException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from periodAssignation";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<PeriodAssignation> periods = new ArrayList<PeriodAssignation>();
						while (rs.next()) {
							PeriodAssignation pa = getPeriodAssignation(rs);
							loadPerson(rs,pa);
							periods.add(pa);
						}
						return periods;
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
			throw new PeriodException(e);
		}
	}
	
	@Override
	public List<PeriodAssignation> findAll(Person p) throws PeriodException {
		if (p.getId() == null) {
			throw new PeriodException("person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from periodAssignation where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,p.getId());
					
					ResultSet rs = st.executeQuery();
					try {
						List<PeriodAssignation> periods = new ArrayList<PeriodAssignation>();
						while(rs.next()) {
							PeriodAssignation pa = getPeriodAssignation(rs);
							pa.setPerson(p);
							periods.add(pa);
						}
						return periods;
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
			throw new PeriodException(e);
		}
	}

	@Override
	public String persist(PeriodAssignation p) throws PeriodException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (p.getId() == null) {
					String id = UUID.randomUUID().toString();
					p.setId(id);
					query = "insert into periodassignation (person_id, pstart, type, id) values (?,?,?,?)";
				} else {
					query = "update periodassignation set person_id = ?, pstart = ?, type = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, p.getPerson().getId());
					st.setTimestamp(2, new Timestamp(p.getStart().getTime()));
					st.setString(3, p.getType().toString());
					st.setString(4, p.getId());
					
					st.executeUpdate();
					return p.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PeriodException(e);
		}
	}

	@Override
	public void remove(PeriodAssignation p) throws PeriodException {
		if (p.getId() == null) {
			throw new PeriodException("periodAssignation.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "delete from periodassignation where (id = ?)";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, p.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new PeriodException(e);
		}
	}
	
	
}
