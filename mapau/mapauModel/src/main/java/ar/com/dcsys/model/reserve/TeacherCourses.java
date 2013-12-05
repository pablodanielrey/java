package ar.com.dcsys.model.reserve;

import java.util.List;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class TeacherCourses implements CoursesForPerson {
	
	/*private final AssignmentsManager assignmentsManager;
	private final AuthsManager authsManager;
	private final PersonsManager personsManager;
	
	public TeacherCourses(AssignmentsManager assignmentsManager, AuthsManager authsManager, PersonsManager personsManager) {
		this.assignmentsManager = assignmentsManager;
		this.authsManager = authsManager;
		this.personsManager = personsManager;
	}
	
	private List<Course> getCourses(List<Assignment> assignmnets) {
		List<Course> courses = new ArrayList<>();
		for (Assignment a : assignmnets) {
			AssignableUnit au = a.getAssignableUnit();
			if (au instanceof Course) {
				courses.add((Course)au);
			}
		}
		return courses;
	}
	
	@Override
	public List<Course> getCourses() throws AuthException, MapauException, PersonException, SilegException {
		try {
			DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person person = personsManager.findByPrincipal(principal);
			if (person == null) {
				throw new PersonException("person == null");
			}
			
			List<Assignment> assignments = assignmentsManager.findBy(person);
			List<Course> courses = getCourses(assignments);
			return courses;
			
		} catch (SilegException e) {
			throw new MapauException(e);
		}		
	}*/  
	
	@Override
	public List<Course> getCourses() throws PersonException, MapauException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
