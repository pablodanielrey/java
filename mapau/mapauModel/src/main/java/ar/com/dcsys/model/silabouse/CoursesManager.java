package ar.com.dcsys.model.silabouse;

import java.util.List;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.exceptions.MapauException;

public interface CoursesManager {

	public String persist(Course course) throws MapauException;
	public List<Course> findAll() throws MapauException;
	public Course findById(String id) throws MapauException;
	public List<Course> findBy(Subject subject) throws MapauException;
	
}
