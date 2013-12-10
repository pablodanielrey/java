package ar.com.dcsys.model.reserve;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.appointment.AppointmentV2Bean;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.types.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.reserve.ReserveAttemptDateTypeBean;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.AreaBean;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.CourseBean;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.data.silabouse.SubjectBean;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.silabouse.AreasManager;
import ar.com.dcsys.model.silabouse.CoursesManager;
import ar.com.dcsys.model.silabouse.SubjectsManager;

public class ReserveAttemptsManagerTest {

	private static Weld weld;
	private static WeldContainer container;
	
	@BeforeClass
	public static void setEnvironment() {
		weld = new Weld();
		container = weld.initialize();
	}

	
	@AfterClass
	public static void destroyEnvironment() {
		if (weld != null) {
			weld.shutdown();
		}
	}
	
	private ReserveAttemptsManager getReserveAttemptsManager() {
		ReserveAttemptsManager reserveAttemptsManager = container.instance().select(ReserveAttemptsManager.class).get();
		return reserveAttemptsManager;
	}
	
    private PersonDAO getPersonDAO() {
        PersonDAO personDAO = container.instance().select(PersonDAO.class).get();
        return personDAO;
    }	
    
    private SubjectsManager getSubjectsManager() {
    	SubjectsManager subjectsManager = container.instance().select(SubjectsManager.class).get();
    	return subjectsManager;
    }	
    
    private CoursesManager getCoursesManager() {
    	CoursesManager coursesManager = container.instance().select(CoursesManager.class).get();
    	return coursesManager;
    }
    
    private ReserveAttemptTypesManager getReserveAttemptTypesManager() {
    	ReserveAttemptTypesManager reserveAttemptTypesManager = container.instance().select(ReserveAttemptTypesManager.class).get();
    	return reserveAttemptTypesManager;
    }
    
    private AreasManager getAreasManager() {
    	AreasManager areasManager = container.instance().select(AreasManager.class).get();
    	return areasManager;
    }
	
    private Group createGroup() {
		Group group = new Group();
		group.setMails(new ArrayList<Mail>());
		group.setName("Grupo creado en el test");
		group.setPersons(new ArrayList<Person>());
		group.setTypes(new ArrayList<GroupType>());
		String id = UUID.randomUUID().toString();
		group.setId(id);    	
		return group;
    }
    
	private Area createArea() throws MapauException {
		AreasManager areasManager = getAreasManager();

		Group group = createGroup();
		List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
		List<Course> courses = new ArrayList<Course>();
		
		Area area = new AreaBean();		
		area.setName("Detise");		
		area.setGroup(group);		
		area.setClassRooms(classRooms);
		area.setCourses(courses);
		
		String id = areasManager.persist(area);
		assertNotNull(id);
		assertNotNull(area);
		
		return area;
	}
	
	private List<CharacteristicQuantity> createCharacteristics() {
		return new ArrayList<CharacteristicQuantity>();
	}
	
	private ClassRoom createClassRoom() {
		return null;
	}

	private Subject createSubject() throws MapauException {
		
		SubjectsManager subjectsManager = getSubjectsManager();
		
		Subject subject = new SubjectBean();
		subject.setName("Conta 1");
		
		String id = subjectsManager.persist(subject);
		
		assertNotNull(subject);
		assertNotNull(id);
		
		return subject;
	}
	
	private Course createCourse() throws MapauException {
		CoursesManager coursesManager = getCoursesManager();
		
		Course course = new CourseBean();
		
		Subject subject = createSubject();		
		course.setSubject(subject);
		course.setName("Catedra B");
		String id = coursesManager.persist(course);
		
		assertNotNull(course);
		assertNotNull(id);
		
		return course;
	}
	
	private Person createPerson() throws PersonException {
		PersonDAO personDAO = getPersonDAO();
		
        String dni = "123456";
        
        Person person = new PersonBean();
        person.setName("Emanuel Joaquin");
        person.setLastName("Pais");
        person.setDni(dni);
        String id = personDAO.persist(person);
        assertNotNull(id);	
        
        return person;
	}
	
	private Reserve createReserve() {
		return null;
	}
	
	private ReserveAttemptDate createReserveAttemptDate() {
		return null;
	}
	
	private ReserveAttemptDateType createReserveAttemptType() throws MapauException {
		ReserveAttemptTypesManager reserveAttemptTypesManager = getReserveAttemptTypesManager();
		
		ReserveAttemptDateType raType = new ReserveAttemptDateTypeBean();
		raType.setDescription("Descripcion del reserve attempt date type");
		raType.setName("Final");
		
		String id = reserveAttemptTypesManager.persist(raType);
		
		assertNotNull(id);
		assertNotNull(raType);
		
		return raType;
	}
	
	@Test
	public void createNewAppointment() throws MapauException, PersonException {
		ReserveAttemptsManager reserveAttemptsManager = getReserveAttemptsManager();

		List<AppointmentV2> appointments = new ArrayList<AppointmentV2>();
		
		Area area = createArea();
		List<CharacteristicQuantity> chars = createCharacteristics();
		ClassRoom classRoom = createClassRoom();
		Course course = createCourse();
		String description = "Descripción";
		Date start = new Date();
		Date end = new Date(start.getTime() + (1000l * 60l * 90l));
		Person owner = createPerson();
		Reserve reserve = createReserve();
		ReserveAttemptDate reserveAttemptDate = createReserveAttemptDate();
		ReserveAttemptDateType reserveAttemptType = createReserveAttemptType();
		List<String> relatedAppointments = new ArrayList<String>();
		List<Person> relatedPersons = new ArrayList<Person>();
		String studentGroup = "Comision 1";
		Boolean visible = true;
		
		AppointmentV2 app = new AppointmentV2Bean();
		
		app.setArea(area);
		app.setCharacteristics(chars);
		app.setClassRoom(classRoom);
		app.setCourse(course);
		app.setDescription(description);
		app.setStart(start);
		app.setEnd(end);
		app.setOwner(owner);
		app.setR(reserve);
		app.setRad(reserveAttemptDate);
		app.setRaType(reserveAttemptType);
		app.setRelatedAppointments(relatedAppointments);
		app.setRelatedPersons(relatedPersons);
		app.setStudentGroup(studentGroup);
		app.setVisible(visible);
		
		appointments.add(app);
		
		reserveAttemptsManager.createNewAppointments(appointments);
	}
}
