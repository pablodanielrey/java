package ar.com.dcsys.data.justification;

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
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PersonException;


public class JustificationPostgresSqlDAO implements JustificationDAO {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(JustificationPostgresSqlDAO.class.getName());
	private final PostgresSqlConnectionProvider cp;
	
	private final Params params;
	
	@Inject
	public JustificationPostgresSqlDAO(PostgresSqlConnectionProvider cp, Params params) {
		this.params = params;
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		createTables();
	}
	
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists justifications (" +
																		"id varchar not null primary key, " +
																		"code varchar not null," +
																		"description varchar not null)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				st = con.prepareStatement("create table if not exists generalJustificationDate (" +
													"id varchar not null primary key, " +
													"justification_id varchar not null references justifications (id)," +
													"jstart timestamp not null," +
													"jend timestamp not null," +
													"notes text)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				st = con.prepareStatement("create table if not exists justificationDate (" +
													"id varchar not null primary key, " +
													"justification_id varchar not null references justifications (id)," +
													"person_id varchar not null," +
													"jstart timestamp not null," +
													"jend timestamp not null," +
													"deleted boolean not null default 'false', " +
													"notes text)");
				try {
					st.execute();
				} finally {
					st.close();
				}
				
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private Justification getJustification(ResultSet rs) throws SQLException {		
		Justification j = new Justification();
		
		j.setCode(rs.getString("code"));
		j.setDescription(rs.getString("description"));
		j.setId(rs.getString("id"));		
		
		return j;
	}
	
	private JustificationDate getJustificationDate(Connection con, ResultSet rs) throws SQLException, JustificationException, PersonException {
		JustificationDate j = new JustificationDate();
		
		j.setId(rs.getString("id"));
		j.setStart(new Date(rs.getTimestamp("jstart").getTime()));
		j.setEnd(new Date(rs.getTimestamp("jend").getTime()));
		j.setNotes(rs.getString("notes"));
		
		String j_id = rs.getString("justification_id");
		j.setJustification(findById(con,j_id));
		
		String p_id = rs.getString("person_id");
		j.setPerson(params.findPersonById(p_id));
		
		return j;
	}
	
	private final GeneralJustificationDate getGeneralJustificationDate (Connection con, ResultSet rs) throws SQLException, JustificationException {
		GeneralJustificationDate j = new GeneralJustificationDate();
		
		j.setId(rs.getString("id"));
		j.setStart(new Date(rs.getTimestamp("jstart").getTime()));
		j.setEnd(new Date(rs.getTimestamp("jend").getTime()));
		j.setNotes(rs.getString("notes"));
		
		String j_id = rs.getString("justification_id");
		j.setJustification(findById(con,j_id));
		
		return j;
	}
	
	
	
	private Justification findById(Connection con, String id) throws SQLException {
		String query = "select * from justifications where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					Justification j = getJustification(rs);
					return j;
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

	@Override
	public Justification findById(String id) throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				return findById(con, id);
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public List<Justification> findAll() throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from justifications";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Justification> justifications = new ArrayList<Justification>();
						while (rs.next()) {
							Justification j = getJustification(rs);
							justifications.add(j);
						}
						return justifications;
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
			throw new JustificationException(e);
		}
	}

	@Override
	public String persist(Justification j) throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (j.getId() == null) {
					String id = UUID.randomUUID().toString();
					j.setId(id);
					query = "insert into justifications (code, description ,id) values (?, ?, ?)";
				} else {
					query = "update justifications set code = ?, description = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, j.getCode());
					st.setString(2, j.getDescription());
					st.setString(3,j.getId());					
					
					st.executeUpdate();
					return j.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public void remove(Justification j) throws JustificationException {
		if (j.getId() == null) {
			throw new JustificationException("justification.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			
			try {
				String query = "delete from justifications where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, j.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public JustificationDate findJustificationDateById(String id) throws JustificationException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from justificationDate where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							JustificationDate j = getJustificationDate(con,rs);
							return j;
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
			throw new JustificationException(e);
		}
	}

	@Override
	public List<JustificationDate> findBy(Person person, Date start, Date end) throws JustificationException, PersonException {
		if (person.getId() == null) {
			throw new JustificationException("person.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from justificationDate where person_id = ? and jstart >= ? and jend >= ? and jstart <= ? and jend <= ? and deleted = false";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,person.getId());
					st.setTimestamp(2, new Timestamp(start.getTime()));
					st.setTimestamp(3, new Timestamp(start.getTime()));
					st.setTimestamp(4, new Timestamp(end.getTime()));
					st.setTimestamp(5, new Timestamp(end.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<JustificationDate> justifications = new ArrayList<JustificationDate>();
						while(rs.next()) {
							JustificationDate j = getJustificationDate(con,rs);
							justifications.add(j);
						}
						return justifications;
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
			throw new JustificationException(e);
		}
	}

	@Override
	public String persist(JustificationDate jd) throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (jd.getId() == null) {
					String id = UUID.randomUUID().toString();
					jd.setId(id);
					query = "insert into justificationDate (jstart, jend, person_id, justification_id, notes, id) values (?,?,?,?,?,?)";
				} else {
					query = "update justificationDate set jstart = ?, jend = ?, person_id = ?, justification_id = ?, notes = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(jd.getStart().getTime()));
					st.setTimestamp(2, new Timestamp(jd.getEnd().getTime()));
					st.setString(3, jd.getPerson().getId());
					st.setString(4, jd.getJustification().getId());
					st.setString(5, jd.getNotes());
					st.setString(6, jd.getId());				
					
					st.executeUpdate();
					return jd.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public void remove(JustificationDate jd) throws JustificationException {
		if (jd.getId() == null) {
			throw new JustificationException("justification.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			
			try {
				String query = "update justificationDate set deleted = 'true' where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, jd.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public GeneralJustificationDate findGeneralJustificationDateById(String id)	throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from generalJustificationDate where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							GeneralJustificationDate gjd = getGeneralJustificationDate(con,rs);
							return gjd;
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
			throw new JustificationException(e);
		}
	}

	@Override
	public List<GeneralJustificationDate> findGeneralJustificationDateBy(Date start, Date end) throws JustificationException {
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from generalJustificationDate where jstart >= ? and jend >= ? and jstart <= ? and jend <= ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(start.getTime()));
					st.setTimestamp(2, new Timestamp(start.getTime()));
					st.setTimestamp(3, new Timestamp(end.getTime()));
					st.setTimestamp(4, new Timestamp(end.getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<GeneralJustificationDate> justifications = new ArrayList<GeneralJustificationDate>();
						while(rs.next()) {
							GeneralJustificationDate j = getGeneralJustificationDate(con,rs);
							justifications.add(j);
						}
						return justifications;
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
			throw new JustificationException(e);
		}
	}

	@Override
	public String persist(GeneralJustificationDate gjd) throws JustificationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (gjd.getId() == null) {
					String id = UUID.randomUUID().toString();
					gjd.setId(id);
					query = "insert into generalJustificationDate (jstart, jend, justification_id, notes, id) values (?,?,?,?,?)";
				} else {
					query = "update generalJustificationDate set jstart = ?, jend = ?, justification_id = ?, notes = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setTimestamp(1, new Timestamp(gjd.getStart().getTime()));
					st.setTimestamp(2, new Timestamp(gjd.getEnd().getTime()));
					st.setString(3, gjd.getJustification().getId());
					st.setString(4, gjd.getNotes());
					st.setString(5, gjd.getId());
					
					st.executeUpdate();
					return gjd.getId();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}

	@Override
	public void removeGeneralJustificationDate(GeneralJustificationDate gjd) throws JustificationException {
		if (gjd.getId() == null) {
			throw new JustificationException("generalJustificationDate.id == null");
		}
		
		try {
			Connection con = cp.getConnection();
			
			try {
				String query = "delete from generalJustificationDate where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, gjd.getId());
					st.executeUpdate();
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new JustificationException(e);
		}
	}
	
	
	
}
