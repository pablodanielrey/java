package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupDAO;
import ar.com.dcsys.data.silabouse.AreaDAO;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.classroom.ClassRoomsManager;
import ar.com.dcsys.model.silabouse.CoursesManager;

public class AreaDAOParams implements AreaDAO.Params {

	private final GroupDAO groupDAO;
	private final CoursesManager coursesManager;
	private final ClassRoomsManager classRoomsManager;
	
	@Inject
	public AreaDAOParams(GroupDAO groupDAO, CoursesManager coursesManager, ClassRoomsManager classRoomsManager) {
		this.groupDAO = groupDAO;
		this.coursesManager = coursesManager;
		this.classRoomsManager = classRoomsManager;
	}
	
	@Override
	public Group findGroupById(String id) throws PersonException {
		return groupDAO.findById(id);
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
