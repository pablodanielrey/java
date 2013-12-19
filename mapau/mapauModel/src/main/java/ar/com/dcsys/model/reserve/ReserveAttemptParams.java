package ar.com.dcsys.model.reserve;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveAttemptParams implements ReserveAttemptDAO.Params {

	@Override
	public String persist(CharacteristicQuantity c) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String persist(ReserveAttemptDate date) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharacteristicQuantity findCharacteristicQuantityById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Area findAreaById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findPersonById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveAttemptDateType findReserveAttemptTypeById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

}
