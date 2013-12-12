package ar.com.dcsys.gwt.mapau.server;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveAttemptDateDAOParams implements ReserveAttemptDateDAO.Params {

	@Override
	public Person findPersonById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveAttemptDateType findReserveAttemptDateTypeById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Area findAreaById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Characteristic findCharacteristicById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}



}
