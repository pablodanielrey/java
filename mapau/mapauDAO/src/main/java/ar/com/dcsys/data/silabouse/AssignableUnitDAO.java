package ar.com.dcsys.data.silabouse;

import java.io.Serializable;

import ar.com.dcsys.exceptions.MapauException;

public interface AssignableUnitDAO extends Serializable {

	public AssignableUnit findById(String id) throws MapauException;	
	public String findType(AssignableUnit au) throws MapauException;
	public String persist(AssignableUnit au) throws MapauException;
	
	public interface Params {
		public Course findCourseById(String id) throws MapauException;
	}
}

