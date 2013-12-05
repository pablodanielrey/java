package ar.com.dcsys.model.reserve;

import java.util.List;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface CoursesForPerson {

	public List<Course> getCourses() throws PersonException, MapauException;
	
}
