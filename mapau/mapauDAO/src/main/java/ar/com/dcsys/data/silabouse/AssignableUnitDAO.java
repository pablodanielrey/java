package ar.com.dcsys.data.silabouse;

import java.io.Serializable;

import ar.com.dcsys.exceptions.MapauException;

public interface AssignableUnitDAO extends Serializable {

	public AssignableUnit findById(String id) throws MapauException;	
	public void initialize() throws MapauException;
	
	public interface Params {
		public Course findCourseById(String id) throws MapauException;
	}
}

