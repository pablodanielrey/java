package ar.com.dcsys.model.reserve;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.classroom.ClassRoomsManager;

public class ReserveParams  implements ReserveDAO.Params {
	
	private final ClassRoomsManager classRoomsManager;
	private final PersonsManager personsManager;
	
	@Inject
	public ReserveParams(ClassRoomsManager classRoomsManager, PersonsManager personsManager) {
		this.classRoomsManager = classRoomsManager;
		this.personsManager = personsManager;
	}
	
    @Override
    public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException {
            return null;//findReserveAttemptDateById(id);
    }

    @Override
    public ClassRoom findClassRoomById(String id) throws MapauException {
            return null;//classRoomsManager.findById(id);
    }

    @Override
    public Person findPersonById(String id) throws PersonException {
            return null;//personsManager.findById(id);
    }		
}
