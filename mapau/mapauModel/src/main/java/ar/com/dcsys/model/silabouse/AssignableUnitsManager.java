package ar.com.dcsys.model.silabouse;

import ar.com.dcsys.data.silabouse.AssignableUnit;
import ar.com.dcsys.exceptions.MapauException;

public interface AssignableUnitsManager {

	public AssignableUnit findById(String id) throws MapauException;
	
}
