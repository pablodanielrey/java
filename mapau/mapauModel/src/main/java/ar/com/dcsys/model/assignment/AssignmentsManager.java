package ar.com.dcsys.model.assignment;

import java.util.List;

import ar.com.dcsys.data.assignment.Assignment;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;

public interface AssignmentsManager {

	public List<Person> findPersonsBy(Course course) throws MapauException;
	public List<Person> findPersonsBy(Course course, String type) throws MapauException;
	public List<Person> getTeachersBy(Course course) throws MapauException;
	public List<Assignment> findBy(Person person) throws MapauException;
	
	public String persist(Assignment assignment) throws MapauException;
	public Assignment findById(String id) throws MapauException;
	
}
