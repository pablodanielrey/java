package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReserveDAO extends Serializable {

	public String persist(Reserve object) throws MapauException;
	public void remove(Reserve object) throws MapauException;	

	public Reserve findById(String id) throws MapauException, PersonException;
	public List<Reserve> findAll() throws MapauException, PersonException;
	public List<Reserve> findAllByDates(Date start, Date end) throws MapauException, PersonException;

	public List<Reserve> findReserveRelatedWithId(String id) throws MapauException;
	
	public List<Reserve> findBy(ReserveAttemptDate date) throws MapauException, PersonException;
	
	public List<Reserve> findAllCollidingWith(Date start, Date end, List<ClassRoom> classRooms) throws MapauException;
	
	public List<Reserve> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException;
	
	
	public void intialize() throws MapauException;
	
	public interface Params {
		public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException;
		public ClassRoom findClassRoomById(String id) throws MapauException;
		public Person findPersonById(String id) throws PersonException;
		public Reserve findReserveById(String id) throws MapauException;
		public List<Reserve> findReserveByRelated(String id) throws MapauException;
		public String persistReserve(Reserve res) throws MapauException;
	}

}
