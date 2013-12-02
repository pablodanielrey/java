package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReserveAttemptDAO extends Serializable {

	public String persist(ReserveAttempt object) throws MapauException;
	public void remove(ReserveAttempt object) throws MapauException;

	public String persist(ReserveAttemptDeleted r) throws MapauException;
	public ReserveAttemptDeleted findReserveAttemptDeletedById(String id) throws MapauException, PersonException;
	
	public ReserveAttempt findById(String id) throws MapauException, PersonException;
	
	public List<ReserveAttempt> findAll() throws MapauException, PersonException;
	public List<ReserveAttempt> findStandingReserveAttemptsBy(Area area) throws MapauException, PersonException;
	
	public boolean updateCommited(ReserveAttempt ra) throws MapauException;

	public void initialize() throws MapauException;
	
	public interface Params {
		public String persist(CharacteristicQuantity c) throws MapauException;
		public String persist(ReserveAttemptDate date) throws MapauException;
		public CharacteristicQuantity findCharacteristicQuantityById(String id) throws MapauException;
		public Area findAreaById(String id) throws MapauException;
		public Course findCourseById(String id) throws MapauException;
		public Person findPersonById(String id) throws PersonException;
		public ReserveAttemptDateType findReserveAttemptTypeById(String id) throws MapauException;
	}
	
}
	