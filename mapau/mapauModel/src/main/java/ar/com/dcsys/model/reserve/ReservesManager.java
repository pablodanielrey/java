package ar.com.dcsys.model.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReservesManager {

	public Reserve createNew(ReserveAttemptDate date, ClassRoom classRoom, String description) throws MapauException;
	public List<Reserve> findAllCollidingWith(Date start, Date end, List<ClassRoom> classRooms) throws MapauException;
	public List<Reserve> findBy(ReserveAttemptDate date) throws MapauException;
	
	
	public String persist(Reserve reserve) throws MapauException;
	public void remove(Reserve reserve) throws MapauException;
	public Reserve findById(String id) throws MapauException, PersonException;
	public List<Reserve> findAllByDates(Date start, Date end) throws MapauException;
	
	
	/**
	 * Obtiene todas las Reservas realizadas que colisionen en algún punto con las fechas indicadas.
	 * 
	 * @param rad
	 * @param dates
	 * @return
	 * @throws MapauException
	 */
	public List<Reserve> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException;	
	
}
