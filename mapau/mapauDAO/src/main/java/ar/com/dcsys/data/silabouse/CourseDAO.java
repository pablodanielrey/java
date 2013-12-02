package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface CourseDAO extends Serializable {

	public String persist(Course object) throws MapauException;
	public List<Course> findAll() throws MapauException;
	public Course findById(String id) throws MapauException;
	public List<Course> findBy(Subject subject) throws MapauException;
	public void initialize() throws MapauException;
	
	public interface Params {
		public Subject findSubjectById(String id) throws MapauException;
	}
}
