package ar.com.dcsys.data.assignment;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface AssignmentDAO extends Serializable {

	public String persist(Assignment object) throws MapauException;
	public void remove(Assignment object) throws MapauException;
	
	public Assignment findById(String id) throws MapauException;
	
	public List<String> findAllIds() throws MapauException;
	public List<Assignment> findAll() throws MapauException;
	
	public List<String> findIdsBy(Person person) throws MapauException;
	public List<Assignment> findBy(Person person) throws MapauException;
	
	public List<String> findIdsBy(Course course) throws MapauException;
	public List<Assignment> findBy(Course course) throws MapauException;
	public List<Assignment> findBy(Course course, String type) throws MapauException;
	
	
	public interface Params {
		public Person findPersonBySilegIdentifiers(String id) throws PersonException;
		/*
		 * TODO: lo tuve que comentar porque no se en que proyecto esta en la nueva estructura
		 */
		//public List<DCSysPrincipal> findPrincipalsByPerson(Person person) throws AuthException;
		public Person findPersonByUsername(String username) throws PersonException;
		public Person findPersonById(String id) throws PersonException;
		
		public Course findCourseById(String id) throws MapauException;
	}
}
