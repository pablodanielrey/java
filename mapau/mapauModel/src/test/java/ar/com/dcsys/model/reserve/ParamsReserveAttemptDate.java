package ar.com.dcsys.model.reserve;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDateDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.classroom.CharacteristicsManager;
import ar.com.dcsys.model.silabouse.AreasManager;
import ar.com.dcsys.model.silabouse.CoursesManager;

public class ParamsReserveAttemptDate implements ReserveAttemptDateDAO.Params {

	private final PersonDAO personDAO;
	private final CoursesManager coursesManager;
	private final ReserveAttemptTypesManager reserveAttemptTypesManager;
	private final AreasManager areasManager;
	private final CharacteristicsManager characteristicsManager;
	
	@Inject
	public ParamsReserveAttemptDate(PersonDAO personDAO, CoursesManager coursesManager, ReserveAttemptTypesManager reserveAttemptTypesManager, AreasManager areasManager, CharacteristicsManager characteristicsManager) {
		this.personDAO = personDAO;
		this.coursesManager = coursesManager;
		this.reserveAttemptTypesManager = reserveAttemptTypesManager;
		this.areasManager = areasManager;
		this.characteristicsManager = characteristicsManager;
	}
	
	@Override
	public Person findPersonById(String id) throws PersonException {
		return personDAO.findById(id);
	}

	@Override
	public ReserveAttemptDateType findReserveAttemptDateTypeById(String id)	throws MapauException {
		return reserveAttemptTypesManager.findById(id);
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		return coursesManager.findById(id);
	}

	@Override
	public Area findAreaById(String id) throws MapauException {
		return areasManager.findById(id);
	}

	@Override
	public Characteristic findCharacteristicById(String id)	throws MapauException {
		return characteristicsManager.findById(id);
	}

}
