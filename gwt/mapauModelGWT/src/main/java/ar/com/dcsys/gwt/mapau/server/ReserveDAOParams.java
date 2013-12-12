package ar.com.dcsys.gwt.mapau.server;

import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveDAOParams implements ReserveDAO.Params {

	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
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
