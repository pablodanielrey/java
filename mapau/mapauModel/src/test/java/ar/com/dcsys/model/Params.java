package ar.com.dcsys.model;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ar.com.dcsys.data.assignment.AssignmentDAO;
import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.classroom.ClassRoomDAO;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.data.reserve.VisibillityDAO;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.AreaDAO;
import ar.com.dcsys.data.silabouse.AssignableUnit;
import ar.com.dcsys.data.silabouse.AssignableUnitDAO;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.CourseDAO;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.data.silabouse.UntouchableSubjectDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.classroom.CharacteristicsManager;
import ar.com.dcsys.model.silabouse.CoursesManager;
import ar.com.dcsys.model.silabouse.SubjectsManager;

@ApplicationScoped
public class Params implements AssignmentDAO.Params,ClassRoomDAO.Params, ReserveAttemptDAO.Params, 
ReserveAttemptDateDAO.Params,ReserveDAO.Params, VisibillityDAO.Params,AssignableUnitDAO.Params,
AreaDAO.Params,CourseDAO.Params,UntouchableSubjectDAO.Params {

    private final CharacteristicsManager characteristicsManager;
    private final PersonDAO personDAO;
	private final SubjectsManager subjectsManager;
	private final CoursesManager coursesManager;
	private final AssignableUnitDAO assignableUnitDAO;
    
	@Inject
	public Params(CharacteristicsManager characteristicsManager, PersonDAO personDAO, SubjectsManager subjectsManager,CoursesManager coursesManager, AssignableUnitDAO assignableUnitDAO) {
		this.characteristicsManager = characteristicsManager;
		this.personDAO = personDAO;
		this.subjectsManager = subjectsManager;
		this.coursesManager = coursesManager;
		this.assignableUnitDAO = assignableUnitDAO;
    }
      
	@Override
	public Characteristic findCharacteristicById(String id)	throws MapauException {
		return characteristicsManager.findById(id);
	}

	@Override
	public Course findCourseById(String id) throws MapauException {
		return coursesManager.findById(id);
	}

	@Override
	public Person findPersonBySilegIdentifiers(String id) throws PersonException {
		return personDAO.findByDni(id);
	}

	@Override
	public Person findPersonByUsername(String username) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findPersonById(String id) throws PersonException {
		return personDAO.findById(id);
	}

	@Override
	public AssignableUnit findAssignableUnitById(String assignableUnitId) throws MapauException {
		return assignableUnitDAO.findById(assignableUnitId);
	}

	@Override
	public String persist(CharacteristicQuantity c) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String persist(ReserveAttemptDate date) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharacteristicQuantity findCharacteristicQuantityById(String id)	throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Area findAreaById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveAttemptDateType findReserveAttemptTypeById(String id)	throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveAttemptDateType findReserveAttemptDateTypeById(String id)	throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group findGroupById(String id) throws PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ar.com.dcsys.data.reserve.GroupVisible.Params getGroupParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id)
			throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassRoom findClassRoomById(String id) throws MapauException {
		return null;
	}

	@Override
	public Reserve findReserveById(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserve> findReserveByRelated(String id) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String persistReserve(Reserve res) throws MapauException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * PARAMS COURSESDAO
	 */
	
	public Subject findSubjectById(String id) throws MapauException {
		return subjectsManager.findById(id);
	}
	public String persist(AssignableUnit au) throws MapauException {
		return assignableUnitDAO.persist(au);
	}
	
	public String findTypeAssignableUnit(AssignableUnit au) throws MapauException {
		return assignableUnitDAO.findType(au);
	}

}
