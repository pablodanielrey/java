package ar.com.dcsys.model.reserve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
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
import ar.com.dcsys.data.classroom.ClassRoomBean;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.types.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonDAO;
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
import ar.com.dcsys.model.classroom.ClassRoomsManager;
import ar.com.dcsys.model.silabouse.AreasManager;
import ar.com.dcsys.model.silabouse.CoursesManager;
import ar.com.dcsys.model.silabouse.SubjectsManager;

public class ReserveAttemptsManagerTest {

	private static Weld weld;
	private static WeldContainer container;
	
	 
	private static final Long hourL = 1000l * 60l * 60l; 
	private static final Long dayL = 24l * hourL;
	private static final Date today = new Date();
	
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
    
    private ClassRoomsManager getClassRoomsManager() {
    	ClassRoomsManager classRoomsManager = container.instance().select(ClassRoomsManager.class).get();
    	return classRoomsManager;
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
    
	private Area createArea(String name,List<Course> courses,List<ClassRoom> classRooms) throws MapauException {
		AreasManager areasManager = getAreasManager();

		Group group = createGroup();
		
		Area area = new AreaBean();		
		area.setName(name);		
		area.setGroup(group);		
		area.setClassRooms(classRooms);
		area.setCourses(courses);
		
		String id = areasManager.persist(area);
		assertNotNull(id);
		assertNotNull(area);
		
		return area;
	}
	
	private ClassRoom createClassRoom(String name, List<CharacteristicQuantity> chars) throws MapauException {
		ClassRoomsManager classRoomsManager = getClassRoomsManager();
		
		ClassRoom classRoom = new ClassRoomBean();
		classRoom.setName(name);		
		classRoom.setCharacteristicQuantity(chars);
		
		String id = classRoomsManager.persist(classRoom);
		assertNotNull(id);
		
		return classRoom;
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
	
	private ReserveAttemptDate createReserveAttemptDate() {
		return null;
	}
	
	private ReserveAttemptDateType createReserveAttemptType(String name) throws MapauException {
		ReserveAttemptTypesManager reserveAttemptTypesManager = getReserveAttemptTypesManager();
		
		ReserveAttemptDateType raType = new ReserveAttemptDateTypeBean();
		raType.setDescription("Descripcion del reserve attempt date type");
		raType.setName(name);
		
		String id = reserveAttemptTypesManager.persist(raType);
		
		assertNotNull(id);
		assertNotNull(raType);
		
		return raType;
	}
	
	private AppointmentV2 createAppointment(Date start, Date end, Course course, ReserveAttemptDateType reserveAttemptType) throws MapauException, PersonException {

		Person owner = createPerson();	

		ClassRoom classRoom = createClassRoom("102", new ArrayList<CharacteristicQuantity>());
		
		List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
		classRooms.add(classRoom);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);		
		Area area = createArea("Detise",courses,classRooms);		

		
		AppointmentV2 app = new AppointmentV2Bean();
		
		app.setArea(area);
		app.setCharacteristics(new ArrayList<CharacteristicQuantity>());
		app.setClassRoom(classRoom);
		app.setCourse(course);
		app.setDescription("Descripción");
		app.setStart(start);
		app.setEnd(end);
		app.setOwner(owner);
		app.setR(null);
		app.setRad(null);
		app.setRaType(reserveAttemptType);
		app.setRelatedAppointments(new ArrayList<String>());
		app.setRelatedPersons(new ArrayList<Person>());
		app.setStudentGroup("Comision 1");
		app.setVisible(true);	
		
		return app;
	}
	
	@Test
	public void createNewAppointment() throws MapauException, PersonException {
		ReserveAttemptsManager reserveAttemptsManager = getReserveAttemptsManager();

		Date startS = new Date(today.getTime() - (dayL*2));
		Date endS = new Date(today.getTime() + (dayL*2));
		List<TransferFilter> filters = new ArrayList<TransferFilter>();
		
		List<AppointmentV2> appointments = reserveAttemptsManager.findAppointmentsV2By(startS, endS, filters);
		
		assertNotNull(appointments);
		int count = appointments.size();
		
		List<AppointmentV2> appointmentsNew = new ArrayList<>();

		//creo el appointment			
		Date start = new Date();
		Date end = new Date(start.getTime() + (1000l * 60l * 90l));	
		Course course = createCourse();
		ReserveAttemptDateType reserveAttemptType = createReserveAttemptType("Final");	
		
		AppointmentV2 app = createAppointment(start, end, course, reserveAttemptType);
		appointmentsNew.add(app);
		
		reserveAttemptsManager.createNewAppointments(appointmentsNew);
		count += 1;
		
		// verifico que lo agrego a la base
		appointments = reserveAttemptsManager.findAppointmentsV2By(startS, endS, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(), count);
	}
	
	
	@Test
	public void findAllAppointmentsBy() throws MapauException, PersonException {
		ReserveAttemptsManager reserveAttemptsManager = getReserveAttemptsManager();
		
		
		List<AppointmentV2> appointmentsNew = new ArrayList<>();
		

		// inicalizo los datos que voy a utilizar
		Course course = createCourse();
		ReserveAttemptDateType reserveAttemptTypeFinal = createReserveAttemptType("Final");	
		ReserveAttemptDateType reserveAttemptTypeParcial = createReserveAttemptType("Parcial");	
		
		Date start = new Date();
		Date end = new Date(start.getTime() + (1000l * 60l * 90l));		
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(start);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		start = c.getTime();
		int shour = c.get(Calendar.HOUR_OF_DAY);
		int smin = c.get(Calendar.MINUTE);
		
		c.setTime(end);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		end = c.getTime();
		int ehour = c.get(Calendar.HOUR_OF_DAY);
		int emin = c.get(Calendar.MINUTE);			
		
		List<Date> dates = new ArrayList<Date>();
		for (int i=0; i < 7; i++) {
			Date date = new Date (start.getTime() + (dayL * new Long(i))); 
			dates.add(date);
		}

		// ************************************************************
		//creo el appointment			
		// ************************************************************

		AppointmentV2 app = createAppointment(start,end,course,reserveAttemptTypeFinal);		
		appointmentsNew.add(app);		
		reserveAttemptsManager.createNewAppointments(appointmentsNew);	
		

		//obtengo el appointment que agregue en la base
		List<AppointmentV2>  appointments = reserveAttemptsManager.findAppointmentsV2By(start, end, new ArrayList<TransferFilter>());
		assertNotNull(appointments);
		assertEquals(appointments.size(), 1);
		AppointmentV2 appOriginal = appointments.get(0);
		assertNotNull(appOriginal.getRad());
		assertNotNull(appOriginal.getCourse());
		
		
		
		//realizo la busqueda inicial e inicializo las variables countNotCheckHour y countCheckHour	
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		int countNotCheckHour = appointments.size();
		
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		int countCheckHour = appointments.size();
		
		

		// ************************************************************
		//creo otro  appointment con los mismos datos que el primero
		// ************************************************************
			
		
		app = createAppointment(start,end, course, reserveAttemptTypeFinal);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		
		reserveAttemptsManager.createNewAppointments(appointmentsNew);		
		
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que se haya incrementado en uno countNotCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countNotCheckHour + 1);
		countNotCheckHour ++;
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico se haya incrementado en uno countCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countCheckHour + 1);
		countCheckHour ++;
		
		
		
		// ************************************************************
		//creo otro appointment, con 2 horas de diferencia
		// ************************************************************
		
		c.setTime(start);
		c.set(Calendar.HOUR_OF_DAY,shour + 2);
		c.set(Calendar.MINUTE,smin);
		start = c.getTime();

		c.setTime(end);
		c.set(Calendar.HOUR_OF_DAY,ehour + 2);
		c.set(Calendar.MINUTE,emin);
		end = c.getTime();		
		
		app = createAppointment(start,end, course,reserveAttemptTypeFinal);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		
		reserveAttemptsManager.createNewAppointments(appointmentsNew);				
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que no se haya incrementado countNotCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countNotCheckHour + 1);
		countNotCheckHour ++;
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico que no se haya incrementado countCheckHour NotCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countCheckHour);
				
		
		// ************************************************************
		// creo otro appointment, pero con otro tipo
		// ************************************************************	
		
		c.setTime(start);
		c.set(Calendar.HOUR_OF_DAY,shour);
		c.set(Calendar.MINUTE, smin);
		start = c.getTime();
		
		c.setTime(end);
		c.set(Calendar.HOUR_OF_DAY,ehour);
		c.set(Calendar.MINUTE, emin);
		end = c.getTime();		
		
		app = createAppointment(start,end, course,reserveAttemptTypeParcial);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		
		reserveAttemptsManager.createNewAppointments(appointmentsNew);		
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que no se haya incrementado countNotCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countNotCheckHour);
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico que no se haya incrementado countCheckHour NotCheckHour
		appointments = reserveAttemptsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countCheckHour);		

		
		
	}
}
