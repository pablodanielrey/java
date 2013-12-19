package ar.com.dcsys.model.reserve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateDAO;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class ReserveAttemptDatesManagerBean implements ReserveAttemptDatesManager {

	private static Logger logger = Logger.getLogger(ReserveAttemptDatesManagerBean.class.getName());
	private final ReserveAttemptDateDAO reserveAttemptDateDAO;
	
	@Inject
	public ReserveAttemptDatesManagerBean(ReserveAttemptDateDAO reserveAttemptDateDAO) {
		this.reserveAttemptDateDAO = reserveAttemptDateDAO;
	}
	
	@Override
	public ReserveAttemptDate findById(String id)	throws MapauException {
		try {
			return reserveAttemptDateDAO.findById(id);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}

	@Override
	public String persist(ReserveAttemptDate date) throws MapauException {
		return reserveAttemptDateDAO.persist(date);
	}
	
	/**
	 * Encuentra las reserveattemptDate entre las fechas pasadas como parámetro.
	 * los reserveattmptdate retornados son los ultimos de la cadena de modificaciones de reserveattemptdate.
	 * o sea los que tienen el reserveattemptdate_id == null!!!. (son los ultimos de la lista).
	 */
	@Override
	public List<ReserveAttemptDate> findBy(Date start, Date end) throws MapauException {
		try {
			return reserveAttemptDateDAO.findBy(start, end);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public void remove(ReserveAttemptDate date) throws MapauException {
		reserveAttemptDateDAO.remove(date);
	}
	
	@Override
	public void persist(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException {
		reserveAttemptDateDAO.persist(characteristicQuantity, reserveAttemptDate);
	}
	
	@Override
	public List<ReserveAttemptDate> findAll(ReserveAttemptDate rad,	List<DatesRange> dates) throws MapauException {
		
		if (rad.getCourse() == null) {
			throw new MapauException("rad.course == null");
		}
		
		if (rad.getType() == null) {
			throw new MapauException("rad.type == null");
		}
		
		if (rad.getStudentGroup() == null) {
			throw new MapauException("rad.studentGroup == null");
		}
		
		
		try {
			List<ReserveAttemptDate> rads = new ArrayList<ReserveAttemptDate>();
			for (DatesRange dr : dates) {
				List<ReserveAttemptDate> rads2 = reserveAttemptDateDAO.findBy(dr.getStart(), dr.getEnd());
				for (ReserveAttemptDate rd : rads2) {
					// el curso, tipo y comisión deben ser iguales.
					if (rad.getCourse().getId().equals(rd.getCourse().getId()) && rad.getType().getId().equals(rd.getType().getId()) && rad.getStudentGroup().equals(rd.getStudentGroup())) {
						rads.add(rd);
					}
				}
			}
			return rads;
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}	
	
	
	@Override
	public void remove(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException {
		reserveAttemptDateDAO.remove(characteristicQuantity, reserveAttemptDate);
	}
	
	
	@Override
	public void removeAllCharacteristics(ReserveAttemptDate reserveAttemptDate)	throws MapauException {
		reserveAttemptDateDAO.removeAllCharacteristics(reserveAttemptDate);
	}	
	
	
	@Override
	public List<ReserveAttemptDate> findAllCollidingWith(List<DatesRange> dates) throws MapauException {
		try {
			return reserveAttemptDateDAO.findAllCollidingWith(dates);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public List<ReserveAttemptDate> findBy(ReserveAttempt ra) throws MapauException {
		try {
			return reserveAttemptDateDAO.findBy(ra);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}

	

}
