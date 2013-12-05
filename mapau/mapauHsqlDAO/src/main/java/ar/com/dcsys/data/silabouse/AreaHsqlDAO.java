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
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class AreaHsqlDAO implements AreaDAO {

	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = Logger.getLogger(AreaHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public AreaHsqlDAO(HsqlConnectionProvider cp, Params params) {
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
			PreparedStatement st = con.prepareStatement("create table if not exists area (" +
																"id longvarchar not null primary key, " +
																"name longvarchar not null," +
																"group_id longvarchar not null)");	
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists area_course (" +
												"area_id longvarchar not null, " +
												"course_id longvarchar not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
			
			st = con.prepareStatement("create table if not exists area_classRoom (" +
												"area_id longvarchar not null, " +
												"classRoom_id longvarchar not null)");
			try {
				st.executeUpdate();
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	private void loadGroup(ResultSet rs, Area area) throws SQLException, PersonException {
		String group_id = rs.getString("group_id");
		area.setGroup(params.findGroupById(group_id));
	}
	
	private void loadAllCourses(Area area, Connection connection) throws SQLException,MapauException  {
	   	PreparedStatement st = connection.prepareStatement("SELECT course_id FROM area_course WHERE area_id = ?");
	   	try {
		   	st.setString(1, area.getId());
		   	ResultSet rs = st.executeQuery();
			try {
			   	while (rs.next()) {
			   		String id = rs.getString("course_id");
			   		Course course = params.findCourseById(id);
					if (course != null) {
						area.getCourses().add(course);
					}	   		
			   	}
			} finally {
				rs.close();
			}
	   	} finally {
	   		st.close();
	   	}
	}	

	private void loadAllClassRooms(Area area, Connection connection) throws SQLException,MapauException  {
	   	PreparedStatement st = connection.prepareStatement("SELECT classRoom_id FROM area_classRoom WHERE area_id = ?");
	   	try {
		   	st.setString(1, area.getId());
		   	ResultSet rs = st.executeQuery();
		   	
		   	try {
			   	while (rs.next()) {
			   		String id = rs.getString("classRoom_id");
			   		ClassRoom classRoom;
					classRoom = params.findClassRoomById(id);
					if (classRoom != null) {
						area.getClassRooms().add(classRoom);
					}
			   	}
		   	} finally {
		   		rs.close();
		   	}
	   	} finally {
	   		st.close();
	   	}
	}	
	
	private Area getArea(ResultSet rs) throws SQLException {
		Area area = new AreaBean();
		area.setName(rs.getString("name"));	
		area.setId(rs.getString("id"));
		return area;		
	}		

	@Override
	public Area findById(String id) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from area where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Area area = getArea(rs);
							loadGroup(rs, area);
							loadAllClassRooms(area, con);
							loadAllCourses(area, con);
							return area;
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
	public List<Area> findAll() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from area";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Area> areas = new ArrayList<Area>();
						while (rs.next()) {
							Area area = getArea(rs);
							loadGroup(rs, area);
							loadAllClassRooms(area, con);
							loadAllCourses(area, con);
							areas.add(area);
						}
						return areas;
					} finally {
						rs.close();
					}
				} finally {
					st.close();
				}
			} finally {
				con.close();
			}
		} catch (SQLException |PersonException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public List<String> findAllIds() throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from area";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> areasIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							areasIds.add(id);
						}
						return areasIds;
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
	public List<String> findIdsByGroup(String groupId) throws MapauException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from area where group_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,groupId);
					ResultSet rs = st.executeQuery();
					try {
						List<String> areasIds = new ArrayList<String>();
						while (rs.next()) {
							String id = rs.getString("id");
							areasIds.add(id);
						}
						return areasIds;
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

	   	String query = "INSERT INTO area_course (area_id, course_id) VALUES (?, ?);" ;
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
	
	private void updateClassRooms(Connection con, String id, List<ClassRoom> classRooms) throws SQLException {
		removeClassRooms(con,id);
			   		
	   	String query = "INSERT INTO area_classRoom (area_id, classRoom_id) VALUES (?, ?);" ;
	   	PreparedStatement st = con.prepareStatement(query) ;
	   	try {
		   	for (ClassRoom classRoom : classRooms) {
			   	st.setString(1, id);
			   	st.setString(2, classRoom.getId());
			   	st.executeUpdate();
		   	}
	   	} finally {
	   		st.close();
	   	}	
	}	

	@Override
	public String persist(Area area) throws MapauException {
		if (area == null) {
			throw new MapauException("area == null");
		}
		if (area.getGroup() == null) {
			throw new MapauException("group == null");
		}		
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (area.getId() == null) {
					String id = UUID.randomUUID().toString();
					area.setId(id);
					query = "INSERT INTO area (name, group_id, id) VALUES (?, ?, ?);";
				} else {
					query = "UPDATE area SET name = ?, group_id = ? WHERE id = ?;";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
				 	st.setString(1, area.getName());
				   	st.setString(2, area.getGroup().getId());
				   	st.setString(3, area.getId());
					st.executeUpdate();
				   
					updateCourses(con,area.getId(),area.getCourses());
					updateClassRooms(con, area.getId(), area.getClassRooms());	
					
					return area.getId();
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
	   	PreparedStatement st = con.prepareStatement("DELETE FROM area_course WHERE area_id = ? ;") ;
	   	try {
		   	st.setString(1, id);
		    st.executeUpdate();	
	   	} finally {
	   		st.close();
	   	}
	}
	
	private void removeClassRooms(Connection con, String id) throws SQLException {
	   	PreparedStatement st = con.prepareStatement("DELETE FROM area_classRoom WHERE area_id = ? ;") ;
	   	try {
		   	st.setString(1, id);
		   	st.executeUpdate();
	   	} finally {
	   		st.close();
	   	}
	}
	
	@Override
	public void remove(Area area) throws MapauException {
		throw new MapauException("Not Implemented");
	}

}
