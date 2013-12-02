package ar.com.dcsys.data.reserve;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface VisibillityDAO {

	public String persist(Visible visible) throws MapauException;
	public void remove(Visible visible) throws MapauException;
	public Visible findBy(ReserveAttemptDate date) throws MapauException;
	
	public void initialize() throws MapauException;
	
	
	public enum Type {
		VISIBLE, GROUPVISIBLE;
	}
	
	public interface Params {
		public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException;
		public Group findGroupById(String id) throws PersonException;
		public GroupVisible.Params getGroupParams();
	}
	
	
	
}
