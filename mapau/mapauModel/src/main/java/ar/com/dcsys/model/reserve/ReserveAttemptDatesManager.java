package ar.com.dcsys.model.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;

public interface ReserveAttemptDatesManager {
	

	public String persist(ReserveAttemptDate date) throws MapauException;
	public void remove(ReserveAttemptDate date) throws MapauException;

	public ReserveAttemptDate findById(String id) throws MapauException;	
	public List<ReserveAttemptDate> findAll(ReserveAttemptDate rad,	List<DatesRange> dates) throws MapauException;
	public List<ReserveAttemptDate> findBy(ReserveAttempt ra) throws MapauException;	
	public List<ReserveAttemptDate> findBy(Date start, Date end) throws MapauException;
	public List<ReserveAttemptDate> findAllCollidingWith(List<DatesRange> dates) throws MapauException;
	
	
	public void persist(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void remove(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void removeAllCharacteristics(ReserveAttemptDate reserveAttemptDate) throws MapauException;	
}
