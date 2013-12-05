package ar.com.dcsys.model.silabouse;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.exceptions.MapauException;

public interface AreasManager {
	public String persist(Area area) throws MapauException ;
	public void remove(Area area) throws MapauException ;
	
	public Area findById(String id) throws MapauException ;
	public List<Area> findAll() throws MapauException ;
	public List<Area> findBy(List<Group> groups) throws MapauException;
}
