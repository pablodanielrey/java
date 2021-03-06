package ar.com.dcsys.data.reserve;

import java.io.Serializable;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface VisibillityDAO extends Serializable {

	public String persist(Visible visible) throws MapauException;
	public void remove(Visible visible) throws MapauException;
	public Visible findBy(ReserveAttemptDate date) throws MapauException;
	
	
	public enum Type {
		VISIBLE, GROUPVISIBLE;
	}
	
	public interface Params {
		public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException;
		public Group findGroupById(String id) throws PersonException;
		public GroupVisible.Params getGroupParams();
	}
	
	
	
}
