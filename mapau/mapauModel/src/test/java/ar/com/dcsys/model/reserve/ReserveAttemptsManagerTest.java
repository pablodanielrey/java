package ar.com.dcsys.model.reserve;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.appointment.AppointmentV2Bean;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

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
	
	private Area createArea() {
		return null;
	}
	
	private List<CharacteristicQuantity> createCharacteristics() {
		return new ArrayList<CharacteristicQuantity>();
	}
	
	private ClassRoom createClassRoom() {
		return null;
	}
	
	private Course createCourse() {
		return null;
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
	
	private ReserveAttemptDateType createReserveAttemptType() {
		return null;
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
		
		//reserveAttemptsManager.createNewAppointments(appointments);
	}
}
