package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface SilegManager {

	public void findAllCourses(Receiver<List<Course>> courses);
	public void findAllTeachers(Receiver<List<Person>> persons);
	
}
