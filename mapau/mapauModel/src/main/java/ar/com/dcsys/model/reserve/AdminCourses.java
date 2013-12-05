package ar.com.dcsys.model.reserve;

import java.util.List;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class AdminCourses implements CoursesForPerson {

/*	private final AuthsManager authsManager; 
	private final PersonsManager personsManager;
	private final GroupsManager groupsManager;
	private final AreasManager aManager;
	
	public AdminCourses(AuthsManager authsManager, PersonsManager personsManager, GroupsManager groupsManager, AreasManager aManager) {
		this.authsManager = authsManager;
		this.personsManager = personsManager;
		this.groupsManager = groupsManager;
		this.aManager = aManager;
	}
	*/
	@Override
	public List<Course> getCourses() throws MapauException, PersonException {
		/*try {
			List<Area> areas = AreasUtilsBean.getAreasFromLoggedUser(authsManager,personsManager,groupsManager,aManager);
			List<Course> courses = new ArrayList<Course>();
			for (Area a : areas) {			
				courses.addAll(a.getCourses());
			}
			return courses;
			
		} catch (AuthException | PersonException e) {
			throw new MapauException(e);
		}
	*/
		return null;
	}
}
