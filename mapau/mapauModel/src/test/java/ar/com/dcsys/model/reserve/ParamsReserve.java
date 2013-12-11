package ar.com.dcsys.model.reserve;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.classroom.ClassRoomsManager;

public class ParamsReserve implements ReserveDAO.Params {

	private final ClassRoomsManager classRoomsManager;
	//private final ReserveAttemptsManager reserveAttemptDatesManager;
	
	@Inject
	public ParamsReserve(ClassRoomsManager classRoomsManager) {
		this.classRoomsManager = classRoomsManager;
	}
	
	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id)	throws MapauException {
		return null;//reserveAttemptDatesManager.findReserveAttemptDateById(id);
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		return classRoomsManager.findById(id);
	}

	@Override
	public Person findPersonById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reserve findReserveById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserve> findReserveByRelated(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String persistReserve(Reserve res) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

}
