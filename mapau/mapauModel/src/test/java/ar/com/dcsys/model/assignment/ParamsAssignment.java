package ar.com.dcsys.model.assignment;

import ar.com.dcsys.data.assignment.AssignmentDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ParamsAssignment implements AssignmentDAO.Params {

	@Override
	public Person findPersonBySilegIdentifiers(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findPersonByUsername(String username) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findPersonById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

}
