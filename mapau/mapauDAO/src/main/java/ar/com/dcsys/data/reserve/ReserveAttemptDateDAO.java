package ar.com.dcsys.data.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReserveAttemptDateDAO {
	
	public void initialize() throws MapauException;
	
	public String persist(ReserveAttemptDate date) throws MapauException;
	public void remove(ReserveAttemptDate date) throws MapauException;
	
	public List<ReserveAttemptDate> findBy(ReserveAttempt ra) throws MapauException, PersonException;
	public ReserveAttemptDate findById(String id) throws MapauException, PersonException;
	
	/**
	 * Retorna todos los ReserveAttemptDate que están incluídos en el rango de fechas pasado como parámetro.
	 * Chequea que sean las últimas!!. esto es. que no tengan related cargado!!!. o sea, lo
	 * retornado es la última modificación del ReserveAttemptDate. 
	 * @param start
	 * @param end
	 * @return
	 * @throws MapauException
	 * @throws PersonException
	 */
	public List<ReserveAttemptDate> findBy(Date start, Date end) throws MapauException, PersonException;
	
	public List<ReserveAttemptDate> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException;	
	
	public interface Params {
		public Person findPersonById(String id) throws PersonException;
		public ReserveAttemptDateType findReserveAttemptDateTypeById(String id) throws MapauException;
		public Course findCourseById(String id) throws MapauException;
		public Area findAreaById(String id) throws MapauException;
		public Characteristic findCharacteristicById(String id) throws MapauException;
	}

}
