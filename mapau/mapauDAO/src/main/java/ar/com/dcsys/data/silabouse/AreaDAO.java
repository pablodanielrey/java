package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface AreaDAO extends Serializable {
	
	public String persist(Area area) throws MapauException ;
	public void remove(Area area) throws MapauException ;
	
	public Area findById(String id) throws MapauException ;
	public List<Area> findAll() throws MapauException ;
	public List<String> findAllIds() throws MapauException;
	public List<String> findIdsByGroup(String groupId) throws MapauException;
	
	
	public interface Params {
		public Group findGroupById(String id) throws PersonException;
		public Course findCourseById(String id) throws MapauException;
		public ClassRoom findClassRoomById(String id) throws MapauException;
	}

}
