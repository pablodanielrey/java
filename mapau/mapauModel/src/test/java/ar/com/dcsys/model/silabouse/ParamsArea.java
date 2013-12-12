package ar.com.dcsys.model.silabouse;

import java.util.ArrayList;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupBean;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.AreaDAO;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.classroom.ClassRoomsManager;

public class ParamsArea implements AreaDAO.Params {

	private final ClassRoomsManager classRoomsManager;
	private final CoursesManager coursesManager;
	
	@Inject
	public ParamsArea(ClassRoomsManager classRoomsManager, CoursesManager coursesManager) {
		this.classRoomsManager = classRoomsManager;
		this.coursesManager = coursesManager;
	}
	
	/*
	 * TODO: ahora devuelvo un Group creado, en lugar de buscarlo por el id, ya que no esta implementado la parte de grupos
	 * @see ar.com.dcsys.data.silabouse.AreaDAO.Params#findGroupById(java.lang.String)
	 */
	@Override
	public Group findGroupById(String id) throws PersonException {
		Group group = new GroupBean();
		group.setMails(new ArrayList<Mail>());
		group.setName("Grupo creado en el params de area");
		group.setPersons(new ArrayList<Person>());
		group.setTypes(new ArrayList<GroupType>());
		group.setId(id);
		return group;
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		return coursesManager.findById(id);
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		return classRoomsManager.findById(id);
	}

}
