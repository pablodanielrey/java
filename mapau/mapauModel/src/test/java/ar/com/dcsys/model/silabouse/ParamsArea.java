package ar.com.dcsys.model.silabouse;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.silabouse.AreaDAO;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ParamsArea implements AreaDAO.Params {

	@Override
	public Group findGroupById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

}
