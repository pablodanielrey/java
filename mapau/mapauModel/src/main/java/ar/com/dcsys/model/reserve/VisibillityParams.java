package ar.com.dcsys.model.reserve;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.reserve.GroupVisible.Params;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.VisibillityDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class VisibillityParams implements VisibillityDAO.Params {

	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group findGroupById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Params getGroupParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
