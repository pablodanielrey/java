package ar.com.dcsys.model.silabouse;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.CourseDAO;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.exceptions.MapauException;

@Singleton
public class CoursesManagerBean implements CoursesManager {


	private static Logger logger = Logger.getLogger(CoursesManager.class.getName());
	
	private final CourseDAO courseDAO;
	
	@Inject
	public CoursesManagerBean(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
		createCaches();
	}
	
	private void createCaches() {
		
	}
	
	@Override
	public List<Course> findAll() throws MapauException {		
		return courseDAO.findAll();
	}

	@Override
	public Course findById(String id) throws MapauException {
		return courseDAO.findById(id);
	}
	
	@Override
	public List<Course> findBy(Subject subject) throws MapauException {
		return courseDAO.findBy(subject);
	}	

	@Override
	public String persist(Course course) throws MapauException {
		return courseDAO.persist(course);
	}
	
}
