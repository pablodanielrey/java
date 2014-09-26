package ar.com.dcsys.data.period;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.data.common.Days;
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
				
				
				st = con.prepareStatement("create table if not exists periodType (" +
						"id varchar not null primary key, " +
						"type varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				
				st = con.prepareStatement("create table if not exists periodTypeDailyParams (" +
						"id varchar not null primary key, " +
						"sun boolean not null, " +
						"mon boolean not null, " +
						"tue boolean not null, " +
						"wed boolean not null, " +
						"thu boolean not null, " +
						"fri boolean not null, " +
						"sat boolean not null)");
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
	
	private PeriodType getPeriodTypeDailyParams(Connection con, String id) throws SQLException {
		String query = "select * from periodTypeDailyParams where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					PeriodTypeDailyParams periodType = new PeriodTypeDailyParams();
					Set<Days> days = new HashSet<Days>();
					
					if (rs.getBoolean("sun")) {
						days.add(Days.SUN);
					}
					if (rs.getBoolean("mon")) {
						days.add(Days.MON);
					}					
					if (rs.getBoolean("tue")) {
						days.add(Days.TUE);
					}					
					if (rs.getBoolean("wed")) {
						days.add(Days.WED);
					}					
					if (rs.getBoolean("thu")) {
						days.add(Days.THU);
					}					
					if (rs.getBoolean("fri")) {
						days.add(Days.FRI);
					}					
					if (rs.getBoolean("sat")) {
						days.add(Days.SAT);
					}	
					
					periodType.setId(id);
					periodType.setDays(days);
					
					return periodType;
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
	
	private PeriodType getPeriodType(Connection con, String id, String type) throws SQLException {		
		PeriodType pt = null;
		
		if (PeriodTypeDailyParams.class.getName().equals(type)) {
			pt = getPeriodTypeDailyParams(con, id);
			return pt;
		}
		
		if (PeriodTypeNull.class.getName().equals(type)) {
			pt = new PeriodTypeNull();
			pt.setId(id);
			return pt;
		}
		
		if (PeriodTypeSystem.class.getName().equals(type)) {
			pt = new PeriodTypeSystem();
		}
		
		if (PeriodTypeWatchman.class.getName().equals(type)) {
			pt = new PeriodTypeWatchman();
		}

		if (pt != null) {
			pt.setId(id);
		} 
		
		return pt;
		
	}
	
	private PeriodType getPeriodType(String typeId) throws SQLException {
		if (typeId == null) {
			return null;
		}
		
		Connection con = cp.getConnection();
		try {
			String query = "select * from periodType where id = ?";
			PreparedStatement st = con.prepareStatement(query);
			try {
				st.setString(1, typeId);
				ResultSet rs = st.executeQuery();
				try {
					if (rs.next()) {
						PeriodType type = getPeriodType(con,rs.getString("id"),rs.getString("type"));
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
			cp.closeConnection(con);
		}		
	}
	
	private PeriodAssignation getPeriodAssignation(ResultSet rs) throws SQLException {
		PeriodAssignation pa = new PeriodAssignation();
		pa.setId(rs.getString("id"));
		pa.setStart(new Date(rs.getTimestamp("pstart").getTime()));
		pa.setType(getPeriodType(rs.getString("type")));
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
					st.setString(3,type.getClass().getName());
					
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
	
	private void persistPeriodTypeDaily(Connection con, PeriodTypeDailyParams type, Boolean isNew) throws SQLException {
		String id = type.getId();
		Map<Days,Boolean> days = new HashMap<Days,Boolean>();
		for(Days day : Days.values()) {			
			days.put(day,type.getDays().contains(day));
		}
		
		String query;
		if (isNew) {
			query = "insert into periodTypeDailyParams (sun, mon, tue, wed, thu, fri, sat, id) values (?,?,?,?,?,?,?,?)";
		} else {
			query = "update periodTypeDailyParams set sun = ?, mon = ?, tue = ?, wed = ?, thu = ?, fri = ?, sat = ? where id = ?";
		}
		
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setBoolean(1, days.get(Days.SUN));
			st.setBoolean(2, days.get(Days.MON));
			st.setBoolean(3, days.get(Days.TUE));
			st.setBoolean(4, days.get(Days.WED));
			st.setBoolean(5, days.get(Days.THU));
			st.setBoolean(6, days.get(Days.FRI));
			st.setBoolean(7, days.get(Days.SAT));
			st.setString(8, id);
			
			st.executeUpdate();
		} finally {
			st.close();
		}
		
	}
	
	private String persistPeriodType(Connection con, PeriodType type) throws SQLException {
		String typeStr = type.getClass().getName();		
		String query;
		Boolean isNew = false;
		if (type.getId() == null) {
			String id = UUID.randomUUID().toString();
			type.setId(id);
			isNew = true;
			query = "insert into periodType (type, id) values (?,?)";
		} else {
			query = "update periodType set type = ? where id = ?";
		}
		
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, typeStr);
			st.setString(2, type.getId());
			
			st.executeUpdate();
			
			if (type instanceof PeriodTypeDailyParams) {
				persistPeriodTypeDaily(con, (PeriodTypeDailyParams)type,isNew);
			}
			return type.getId();
		} finally {
			st.close();
		}
		
	}
	
	@Override
	public String persist(PeriodAssignation p) throws PeriodException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				Boolean isNew = false;
				if (p.getId() == null) {
					String id = UUID.randomUUID().toString();
					p.setId(id);
					isNew = true;
					query = "insert into periodassignation (person_id, pstart, type, id) values (?,?,?,?)";
				} else {
					query = "update periodassignation set person_id = ?, pstart = ?, type = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, p.getPerson().getId());
					st.setTimestamp(2, new Timestamp(p.getStart().getTime()));
					
					String typeId = persistPeriodType(con, p.getType());
					p.getType().setId(typeId);
					
					st.setString(3, p.getType().getId());
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
