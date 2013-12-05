package ar.com.dcsys.model.silabouse;

import java.util.logging.Logger;

import javax.inject.Singleton;

import ar.com.dcsys.data.silabouse.AssignableUnit;
import ar.com.dcsys.data.silabouse.AssignableUnitDAO;
import ar.com.dcsys.exceptions.MapauException;

@Singleton
public class AssignableUnitsManagerBean implements AssignableUnitsManager {

	private static Logger logger = Logger.getLogger(AssignableUnitsManagerBean.class.getName());
	
	private final AssignableUnitDAO assignableUnitDAO;
	
	
	public AssignableUnitsManagerBean(AssignableUnitDAO assignableUnitDAO) {
		this.assignableUnitDAO = assignableUnitDAO;
	}

	@Override
	public AssignableUnit findById(String id) throws MapauException {
		return assignableUnitDAO.findById(id);
	}

	
}
