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

public class CourseHsqlDAO implements CourseDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(CourseHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public CourseHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
	
	public void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists course (" +
															   	"id longvarchar NOT NULL PRIMARY KEY," +
													   			"name longvarchar NOT NULL ," +
													   			"version BIGINT NULL);");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
   			st = con.prepareStatement("CREATE TABLE IF NOT EXISTS course_subject (" +
									   			"course_id longvarchar NOT NULL ,"+
									   			"subject_id longvarchar NOT NULL);");	
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}		
		} finally {
			con.close();
		}
	}
	
	private void loadSubject(Connection con, Course course) throws SQLException, MapauException {
		String query = "SELECT subject_id FROM course_subject  WHERE course_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, course.getId());
			ResultSet rs = st.executeQuery();
			try {
				if (rs.next()) {
					String id = rs.getString("id");
					Subject subject = params.findSubjectById(id);
					course.setSubject(subject);
				}
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}
	
	private Course getCourse(ResultSet rs) throws SQLException {
   		String id = rs.getString("id");
   		String name = rs.getString("name");
   		Long version = rs.getLong("version");
   		
   		Course course = new CourseBean();
   		course.setId(id);
   		course.setName(name);
   		course.setVersion(version);

		return course;
	}
	
	private void loadType(Course course) throws MapauException {
		String type = params.findTypeAssignableUnit(course);
		course.setType(type);
	}

	@Override
	public Course findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM course WHERE id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Course course = getCourse(rs);
							loadSubject(con, course);
							loadType(course);
							return course;
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
	public List<Course> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT * FROM course";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Course> courses = new ArrayList<Course>();						
						while (rs.next()) {
							Course course = getCourse(rs);
							loadSubject(con, course);
							loadType(course);
							courses.add(course);
						}
						return courses;
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
	public List<Course> findBy(Subject subject) throws MapauException {
		if (subject == null) {
			throw new MapauException("subject == null");
		}
		try {
			Connection con = cp.getConnection();
			try {
				String query = "SELECT c.id, c.name, c.version FROM course c INNER JOIN course_subject cs ON (c.id = cs.course_id) WHERE cs.subject_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, subject.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<Course> courses = new ArrayList<Course>();						
						while (rs.next()) {
							Course course = getCourse(rs);
							loadSubject(con, course);
							loadType(course);
							courses.add(course);
						}
						return courses;
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
	public String persist(Course course) throws MapauException {
		if (course == null) {
			throw new MapauException("course == null");
		}
		if (course.getSubject() == null) {
			throw new MapauException("course.subject == null");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (course.getId() == null) {
					String id = UUID.randomUUID().toString();
					course.setId(id);
					query = "INSERT INTO course (name, version, id) VALUES (?, ?, ?);";
				} else {
					query = "UPDATE course SET name = ?, version = ? WHERE course.id = ?;";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
				 	st.setString(1, course.getName());
				   	st.setLong(2, course.getVersion());
				   	st.setString(3, course.getId());
				   	st.executeUpdate();				
				   	
				   	persistAssignableUnit(course);
				   	persistSubject(con, course);
				   	
				   	return course.getId();
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
	
	private void removeSubjects(Connection con, Course course) throws SQLException {
		String query = "DELETE FROM course_subject WHERE course_id = ?;";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, course.getId());
			st.executeUpdate();
		} finally {
			st.close();
		}
	}
	
	private void persistSubject(Connection con, Course course) throws SQLException {
		removeSubjects(con, course);
		
		String query = "INSERT INTO course_subject (subject_id, course_id) VALUES (?, ?);";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setString(1, course.getSubject().getId());
			st.setString(2, course.getId());
			st.executeUpdate();
		} finally {
			st.close();
		}
	}
	
	private void persistAssignableUnit(Course course) throws MapauException {
		course.setType(course.getClass().getName());
		params.persist(course);
	}

}
