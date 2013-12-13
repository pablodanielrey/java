package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.classroom.ClassRoomsManager;
import ar.com.dcsys.model.reserve.ReserveAttemptsManager;

public class ReserveDAOParams implements ReserveDAO.Params {
	
	private final ReserveAttemptsManager reserveAttemptsManager;
	private final ClassRoomsManager classRoomsManager;
	private final PersonsManager personsManager;
	
	@Inject
	public ReserveDAOParams(ReserveAttemptsManager reserveAttemptsManager, ClassRoomsManager classRoomsManager, PersonsManager personsManager) {
		this.reserveAttemptsManager = reserveAttemptsManager;
		this.classRoomsManager = classRoomsManager;
		this.personsManager = personsManager;
	}

	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id)	throws MapauException {
		return reserveAttemptsManager.findReserveAttemptDateById(id);
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		return classRoomsManager.findById(id);
	}

	@Override
	public Person findPersonById(String id) throws PersonException {
		return personsManager.findById(id);
	}

}
