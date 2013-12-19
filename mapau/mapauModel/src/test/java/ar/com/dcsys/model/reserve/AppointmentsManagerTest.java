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
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupBean;
import ar.com.dcsys.data.group.GroupType;
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

public class AppointmentsManagerTest {

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
	
	private AppointmentsManager getAppointmentsManager() {
		AppointmentsManager appointmentsManager = container.instance().select(AppointmentsManager.class).get();
		return appointmentsManager;
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
		Group group = new GroupBean();
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

	private Subject createSubject(String name) throws MapauException {
		
		SubjectsManager subjectsManager = getSubjectsManager();
		
		Subject subject = new SubjectBean();
		subject.setName(name);
		
		String id = subjectsManager.persist(subject);
		
		assertNotNull(subject);
		assertNotNull(id);
		
		return subject;
	}
	
	private Course createCourse(String nameSubject, String nameCourse) throws MapauException {
		CoursesManager coursesManager = getCoursesManager();
		
		Course course = new CourseBean();
		
		Subject subject = createSubject(nameSubject);		
		course.setSubject(subject);
		course.setName(nameCourse);
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
		AppointmentsManager appointmentsManager = getAppointmentsManager();

		List<TransferFilter> filters = new ArrayList<TransferFilter>();
		Course course = createCourse("Conta I", "Catedra A");
		TransferFilter tfCourse = createTransferFilterCourse(course);
		filters.add(tfCourse);
		
		Calendar c = Calendar.getInstance();
		
		Date start = new Date();	
		c.setTime(start);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		start = c.getTime();

		Date end = new Date(start.getTime() + (1000l * 60l * 90l));
		c.setTime(end);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		end = c.getTime();			
		
		List<AppointmentV2> appointments = appointmentsManager.findAppointmentsV2By(start, end, filters);
		
		assertNotNull(appointments);
		int count = appointments.size();
		assertEquals(count, 0);
		
		List<AppointmentV2> appointmentsNew = new ArrayList<>();

		//creo el appointment			
		ReserveAttemptDateType reserveAttemptType = createReserveAttemptType("Final");	
		Person owner = createPerson();	

		ClassRoom classRoom = createClassRoom("102", new ArrayList<CharacteristicQuantity>());
		
		List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
		classRooms.add(classRoom);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);		
		Area area = createArea("Detise",courses,classRooms);		
		String description = "Descripción";
		String studentGroup = "Comision 1";
		
		AppointmentV2 app = new AppointmentV2Bean();
		
		app.setArea(area);
		app.setCharacteristics(new ArrayList<CharacteristicQuantity>());
		app.setCourse(course);
		app.setDescription(description);
		app.setStart(start);
		app.setEnd(end);
		app.setOwner(owner);
		app.setR(null);
		app.setRad(null);
		app.setRaType(reserveAttemptType);
		app.setStudentGroup(studentGroup);		
		app.setRelatedAppointments(new ArrayList<String>());
		app.setRelatedPersons(new ArrayList<Person>());
		
		appointmentsNew.add(app);
		
		appointmentsManager.createNewAppointments(appointmentsNew);
		
		// verifico que lo agrego a la base
		appointments = appointmentsManager.findAppointmentsV2By(start, end, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(), count + 1);
		
		app = appointments.get(0);		
		assertNotNull(app);
		
		assertNotNull(app.getArea());
		assertEquals(app.getArea().getId(), area.getId());
		
		assertNotNull(app.getCharacteristics());
		assertEquals(app.getCharacteristics().size(),0);
		
		assertNotNull(app.getCourse());
		assertEquals(course.getId(), app.getCourse().getId());
		
		assertEquals(app.getDescription(), description);
		
		assertNotNull(app.getStart());
		assertEquals(app.getStart().getTime(),start.getTime());
		
		assertNotNull(app.getEnd());
		assertEquals(app.getEnd().getTime(),end.getTime());
				
		assertNotNull(app.getRad());
		
		assertNotNull(app.getRaType());
		assertEquals(app.getRaType().getId(), reserveAttemptType.getId());
		
		assertEquals(app.getStudentGroup(), studentGroup);
		
	}
	
	
	@Test
	public void findAllAppointmentsBy() throws MapauException, PersonException {
		AppointmentsManager appointmentsManager = getAppointmentsManager();
		
		
		List<AppointmentV2> appointmentsNew = new ArrayList<>();
		

		// inicalizo los datos que voy a utilizar
		Course course = createCourse("Conta I", "Catedra A");
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
		appointmentsManager.createNewAppointments(appointmentsNew);	
		

		//obtengo el appointment que agregue en la base
		List<AppointmentV2>  appointments = appointmentsManager.findAppointmentsV2By(start, end, new ArrayList<TransferFilter>());
		assertNotNull(appointments);
		assertEquals(appointments.size(), 1);
		AppointmentV2 appOriginal = appointments.get(0);
		assertNotNull(appOriginal.getRad());
		assertNotNull(appOriginal.getCourse());
		
		
		
		//realizo la busqueda inicial e inicializo las variables countNotCheckHour y countCheckHour	
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		int countNotCheckHour = appointments.size();
		
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		int countCheckHour = appointments.size();
		
		

		// ************************************************************
		//creo otro  appointment con los mismos datos que el primero
		// ************************************************************
			
		
		app = createAppointment(start,end, course, reserveAttemptTypeFinal);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		
		appointmentsManager.createNewAppointments(appointmentsNew);		
		
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que se haya incrementado en uno countNotCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countNotCheckHour + 1);
		countNotCheckHour ++;
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico se haya incrementado en uno countCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, true);
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
		
		appointmentsManager.createNewAppointments(appointmentsNew);				
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que  se haya incrementado countNotCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		
		assertEquals(appointments.size(), countNotCheckHour + 1);
		countNotCheckHour ++;
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico que no se haya incrementado countCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, true);
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
		
		appointmentsManager.createNewAppointments(appointmentsNew);		
		
		// realizo la busqueda nuevamente con el checkHour en false y verifico que no se haya incrementado countNotCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, false);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countNotCheckHour);
		
		// realizo la busqueda nuevamente con el checkHour en true y verifico que no se haya incrementado countCheckHour NotCheckHour
		appointments = appointmentsManager.findAllAppointmentsBy(appOriginal, dates, true);
		assertNotNull(appointments);
		assertEquals(appointments.size(), countCheckHour);		
		
	}
	
	@Test
	public void findAllFilters() throws MapauException {
		AppointmentsManager appointmentsManager = getAppointmentsManager();
		
		List<TransferFilterType> filters = appointmentsManager.findAllFilters();
		
		assertNotNull(filters);
			
	}
	
	
	private class TransferFilterImp implements TransferFilter {
		private TransferFilterType type;
		private String param;
		
		@Override
		public TransferFilterType getType() {
			return type;
		}

		@Override
		public void setType(TransferFilterType type) {
			this.type = type;
		}

		@Override
		public String getParam() {
			return param;
		}

		@Override
		public void setParam(String param) {
			this.param = param;
		}
		
	}
	
	private TransferFilter createTransferFilterDate(Date start, Date end) {

		TransferFilter tfp = new TransferFilterImp();
        
        tfp.setType(TransferFilterType.DATE);
        
        String startStr = String.valueOf(start.getTime());
        String endStr = String.valueOf(end.getTime());
        tfp.setParam(startStr + ";" + endStr);
        
        return tfp;		
	}
	
	private TransferFilter createTransferFilterCourse(Course c) {

		TransferFilter tfp = new TransferFilterImp();
        
        tfp.setType(TransferFilterType.COURSE);        
        tfp.setParam(c.getId());
        
        return tfp;		
	}
	
	private TransferFilter createTransferFilterClassRoom(ClassRoom cr) {

		TransferFilter tfp = new TransferFilterImp();
        
        tfp.setType(TransferFilterType.CLASSROOM);        
        tfp.setParam(cr.getId());
        
        return tfp;		
	}
	
	private Date setTime(Date date, int hour, int min, int sec, int mili) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND,sec);
		c.set(Calendar.MILLISECOND,mili);
		return c.getTime();		
	}
	
	@Test
	public void findAppointmentsV2ByFilters() throws MapauException, PersonException {
		AppointmentsManager appointmentsManager = getAppointmentsManager();
		

		ReserveAttemptDateType reserveAttemptTypeFinal = createReserveAttemptType("Final");	
		
		//creo el filtro de fecha 
		Date start = new Date();
		Date end = new Date(start.getTime() + (dayL * 7));
		
		start = setTime(start, 0, 0, 0, 0);
		end = setTime(end, 23, 59, 59, 999);	
		
		TransferFilter tfDate = createTransferFilterDate(start, end);
		
		
		//creo el filtro de course
		Course course = createCourse("Conta I", "Catedra A");
		assertNotNull(course);
		assertNotNull(course.getId());
		TransferFilter tfCourse = createTransferFilterCourse(course);
		
		// cargo los filtros y realizo la busqueda
		List<TransferFilter> filters = new ArrayList<TransferFilter>();
		filters.add(tfDate);
		filters.add(tfCourse);
		
		List<AppointmentV2> appointments = appointmentsManager.findAppointmentsV2By(filters);
		
		int count = appointments.size();
		assertEquals(count, 0);
		filters = new ArrayList<TransferFilter>();
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		int countAll = appointments.size();

		/* **********************************************************************************************************************
		 * creo un appointment que tenga el course creado al inicio y que este entre las fechas start y end
		 * ******************************************************************************************************************** */
		Date newStart = new Date();
		newStart = setTime(newStart, 8, 0, 0, 0);
		Date newEnd = new Date();
		newEnd = setTime(newEnd, 11, 30, 0, 0);
		
		List<AppointmentV2> appointmentsNew = new ArrayList<AppointmentV2>();
		AppointmentV2 app = createAppointment(newStart, newEnd, course, reserveAttemptTypeFinal);
		appointmentsNew.add(app);
		appointmentsManager.createNewAppointments(appointmentsNew);
		
		//realizo la busqueda con el filtro de fecha
		filters = new ArrayList<TransferFilter>();
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);		
		assertEquals(appointments.size(), countAll + 1);		
		
		//realizo la busqueda con todos los filtros
		filters = new ArrayList<TransferFilter>();
		filters.add(tfCourse);
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		assertEquals(appointments.size(), count + 1);	
		
		
		/* **********************************************************************************************************************
		 * creo un appointment que tenga un nuevo course y que este entre las fechas start y end
		 * ******************************************************************************************************************** */
		newStart = new Date();
		newStart = setTime(newStart, 12, 0, 0, 0);
		newEnd = new Date();
		newEnd = setTime(newEnd, 15, 30, 0, 0);
		
		Course courseNew = createCourse("Administracion I", "Catedra B");
		
		app = createAppointment(newStart, newEnd, courseNew, reserveAttemptTypeFinal);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		appointmentsManager.createNewAppointments(appointmentsNew);		
		
		//realizo la busqueda con el filtro de fecha
		filters = new ArrayList<TransferFilter>();
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		assertEquals(appointments.size(), countAll + 2);		
		
		//realizo la busqueda con todos los filtros
		filters = new ArrayList<TransferFilter>();
		filters.add(tfCourse);
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		assertEquals(appointments.size(), count + 1);	
		

		/* **********************************************************************************************************************
		 * creo un appointment que tenga el course creado al inicio y que no este entre las fechas start y end
		 * ******************************************************************************************************************** */
		newStart = new Date(end.getTime() + dayL);
		newStart = setTime(newStart, 8, 0, 0, 0);
		newEnd = new Date(newStart.getTime());
		newEnd = setTime(newEnd, 12, 0, 0, 0);
		
		app = createAppointment(newStart, newEnd, course, reserveAttemptTypeFinal);
		appointmentsNew.clear();
		appointmentsNew.add(app);
		appointmentsManager.createNewAppointments(appointmentsNew);	
		
		//realizo la busqueda con el filtro de fecha
		filters = new ArrayList<TransferFilter>();
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		assertEquals(appointments.size(), countAll + 2);		
		
		//realizo la busqueda con todos los filtros
		filters = new ArrayList<TransferFilter>();
		filters.add(tfCourse);
		filters.add(tfDate);
		appointments = appointmentsManager.findAppointmentsV2By(filters);
		assertEquals(appointments.size(), count + 1);	
		
		
		
	}
	
	
	@Test
	public void modifyAndDeleteAppointment() throws MapauException, PersonException {
		AppointmentsManager appointmentsManager = getAppointmentsManager();

		List<TransferFilter> filters = new ArrayList<TransferFilter>();
		Course course = createCourse("Conta I", "Catedra A");
		TransferFilter tfCourse = createTransferFilterCourse(course);
		filters.add(tfCourse);
		
		Calendar c = Calendar.getInstance();
		
		Date start = new Date();	
		c.setTime(start);
		c.set(Calendar.MILLISECOND, 333);
		start = c.getTime();
		
		Date end = new Date(start.getTime() + (1000l * 60l * 95l));
		c.setTime(end);
		c.set(Calendar.MILLISECOND, 987);
		end = c.getTime();
		
		
		List<AppointmentV2> appointments = appointmentsManager.findAppointmentsV2By(start, end, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(),0);
		
		//creo el appointment
		ReserveAttemptDateType reserveAttemptType = createReserveAttemptType("Final");
		AppointmentV2 app = createAppointment(start, end, course, reserveAttemptType);
		appointments = new ArrayList<AppointmentV2>();
		appointments.add(app);
		appointmentsManager.createNewAppointments(appointments);
		
		//vertifico que lo haya insertado
		appointments = appointmentsManager.findAppointmentsV2By(start, end, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(),1);
		
		//obtengo el appointment insertado y lo modifico
		app = appointments.get(0);
		assertNotNull(app.getRad());
		
		Date newStart = new Date(start.getTime() + hourL);
		Date newEnd = new Date(end.getTime() + hourL); 
		List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
		ClassRoom cr = createClassRoom("306", new ArrayList<CharacteristicQuantity>());
		List<Course> courses = new ArrayList<Course>();
		Course newCourse = createCourse("Conta II", "Catedra C");
		Area area = createArea("SALAPROF", courses, classRooms);		
		String description = "AppointmentModify";
		String studentGroup = "Comision 8";
		ReserveAttemptDateType newReserveAttemptType = createReserveAttemptType("Parcial");
		
		app.setArea(area);
		app.setCharacteristics(new ArrayList<CharacteristicQuantity>());
		app.setCourse(newCourse);
		app.setDescription(description);
		app.setStart(newStart);
		app.setEnd(newEnd);
		app.setRaType(newReserveAttemptType);
		app.setStudentGroup(studentGroup);
		
		assertNotNull(app.getRad());
		appointmentsManager.modify(app);
		
		//busco el appointment con la fecha anterior
		appointments = appointmentsManager.findAppointmentsV2By(start, end, filters);
		assertNotNull(appointments);
		//me tiene que dar 0 ya que no lo deberia encontrar porque fue modificada la fecha
		assertEquals(appointments.size(),0);
		//busco el appointment con la nueva fecha
		//agrego el nuevo course como fultro
		tfCourse = createTransferFilterCourse(newCourse);
		filters = new ArrayList<>();
		filters.add(tfCourse);		
		appointments = appointmentsManager.findAppointmentsV2By(newStart, newEnd, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(),1);
		
		app = appointments.get(0);
		
		
		//verifico que se haya modificado
		assertNotNull(app.getArea());
		assertEquals(app.getArea().getId(), area.getId());
		
		assertNotNull(app.getCharacteristics());
		assertEquals(app.getCharacteristics().size(),0);
		
		assertNotNull(app.getCourse());
		assertEquals(newCourse.getId(), app.getCourse().getId());
		
		assertEquals(app.getDescription(), description);
		
		assertNotNull(app.getStart());
		assertEquals(app.getStart().getTime(),newStart.getTime());
		
		assertNotNull(app.getEnd());
		assertEquals(app.getEnd().getTime(),newEnd.getTime());
				
		assertNotNull(app.getRad());
		
		assertNotNull(app.getRaType());
		assertEquals(app.getRaType().getId(), newReserveAttemptType.getId());
		
		assertEquals(app.getStudentGroup(), studentGroup);		
		
		
		//ahora lo elimino
		appointmentsManager.deleteAppointment(app);
		appointments = appointmentsManager.findAppointmentsV2By(newStart, newEnd, filters);
		assertNotNull(appointments);
		assertEquals(appointments.size(),0);
	}
	
}
