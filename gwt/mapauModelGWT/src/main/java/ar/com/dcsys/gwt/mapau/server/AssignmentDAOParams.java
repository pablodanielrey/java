package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.assignment.AssignmentDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.silabouse.CoursesManager;

public class AssignmentDAOParams implements AssignmentDAO.Params {

	private final PersonsManager personsManager;
	private final CoursesManager coursesManager;
	
	@Inject
	public AssignmentDAOParams(PersonsManager personsManager, CoursesManager coursesManager) {
		this.personsManager = personsManager;
		this.coursesManager = coursesManager;
	}
	
	
	@Override
	public Person findPersonBySilegIdentifiers(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findPersonByUsername(String username) throws PersonException {
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
