package ar.com.dcsys.data.silabouse;

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

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.exceptions.MapauException;

public class UntouchableSubjectHsqlDAO implements UntouchableSubjectDAO {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(UntouchableSubjectHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public UntouchableSubjectHsqlDAO(HsqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@PostConstruct
	void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists untouchablesubjects (" +
																"id longvarchar not null primary key, " +
																"area_id longvarchar not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists untouchablesubjects_course (" +
												"untouchablesubjects_id longvarchar not null, " +
												"course_id longvarchar not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}


	private void loadArea(ResultSet rs, UntouchableSubject untouchableSubjects) throws MapauException, SQLException {
		String area_id = rs.getString("area_id");
		untouchableSubjects.setArea(params.findAreaById(area_id));
	}
	
	private void loadAllCourses(UntouchableSubject untouchableSubjects, Connection con) throws SQLException, MapauException {
		PreparedStatement st = con.prepareStatement("SELECT course_id FROM untouchablesubjects_course WHERE untouchablesubjects_id = ?");
		try {
			st.setString(1, untouchableSubjects.getId());
			ResultSet rs = st.executeQuery();
			try {
				while (rs.next()) {
					String id = rs.getString("course_id");
					Course course = params.findCourseById(id);
					if (course != null) {
						untouchableSubjects.getCourses().add(course);
					}
				}
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}	

	private UntouchableSubject getUntouchableSubjects(ResultSet rs) throws SQLException {
		UntouchableSubject untouchableSubjects = new UntouchableSubjectBean();
		untouchableSubjects.setId(rs.getString("id"));
		return untouchableSubjects;
	}	
	
	@Override
	public UntouchableSubject findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from untouchablesubjects where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							UntouchableSubject us = getUntouchableSubjects(rs);
							loadAllCourses(us, con);
							loadArea(rs, us);
							return us;
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
	public List<UntouchableSubject> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from untouchablesubjects";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<UntouchableSubject> untouchableSubjects = new ArrayList<UntouchableSubject>();
						while (rs.next()) {
							UntouchableSubject us = getUntouchableSubjects(rs);
							loadAllCourses(us, con);
							loadArea(rs, us);
							untouchableSubjects.add(us);
						}
						return untouchableSubjects;
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
	public List<String> findAllIds() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from untouchablesubjects";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> untouchableSubjectsIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							untouchableSubjectsIds.add(id);
						}
						return untouchableSubjectsIds;
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
	public String findIdByArea(String areaId) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from untouchablesubjects where area_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, areaId);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							return rs.getString("id");
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
	
	private void updateCourses(Connection con, String id, List<Course> courses) throws SQLException {
		removeCourses(con,id);

	   	String query = "INSERT INTO untouchablesubjects_course (untouchablesubjects_id, course_id) VALUES (?, ?);" ;
	   	PreparedStatement st = con.prepareStatement(query) ;
	   	try {
		   	for (Course course : courses) {	   		
		   		st.setString(1, id);
		   		st.setString(2, course.getId());
		   		st.executeUpdate();	   		
		   	}	
	   	} finally {
	   		st.close();
	   	}
	}		

	@Override
	public String persist(UntouchableSubject untouchableSubjects) throws MapauException {
		if (untouchableSubjects == null) {
			throw new MapauException("untouchableSubjects == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (untouchableSubjects.getId() == null) {
					String id = UUID.randomUUID().toString();
					untouchableSubjects.setId(id);
					query = "INSERT INTO untouchablesubjects (area_id, id) VALUES (?, ?);";
				} else {
					query = "UPDATE untouchableSubjects SET area_id = ? WHERE id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, untouchableSubjects.getArea().getId());
					st.setString(2, untouchableSubjects.getId());
					st.executeUpdate();	
					
					updateCourses(con, untouchableSubjects.getId(), untouchableSubjects.getCourses());
					
					return untouchableSubjects.getId();
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
	
	private void removeCourses(Connection con, String id) throws SQLException {
	   	PreparedStatement st = con.prepareStatement("DELETE FROM untouchablesubjects_course WHERE untouchablesubjects_id = ? ;") ;
	   	try {
		   	st.setString(1, id);
		   	st.executeUpdate();	
	   	} finally {
	   		st.close();
	   	}
	}	

	@Override
	public void remove(UntouchableSubject untouchableSubjects) throws MapauException {
		throw new MapauException("Not Implemented");
	}

}
