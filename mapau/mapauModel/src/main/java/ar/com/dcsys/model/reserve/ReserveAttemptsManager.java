package ar.com.dcsys.model.reserve;

import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDeleted;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReserveAttemptsManager {
	
	

	
	
	public void setVisible(ReserveAttemptDate rad) throws MapauException;
	public void setVisible(ReserveAttemptDate rad, List<Group> groups) throws MapauException;
	public void removeVisible(ReserveAttemptDate rad) throws MapauException;


		

	//////////////// operacion de confirmacion de visibilidad //////////////
	
	
	public List<ReserveAttempt> getReserveAttemptsToConfirmReserve() throws MapauException;
	
	//////////////////////////////////////////////////////////
	
	
	public String persist(ReserveAttemptDeleted rd) throws MapauException;
	public ReserveAttemptDeleted findReserveAttemptDeletedById(String id) throws MapauException, PersonException;
	
	
	
}
