package ar.com.dcsys.model.reserve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentBean;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.appointment.AppointmentV2Bean;
import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateBean;
import ar.com.dcsys.data.reserve.ReserveAttemptDateDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.reserve.ReserveAttemptDeleted;
import ar.com.dcsys.data.reserve.ReserveBean;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.UntouchableSubject;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.classroom.ClassRoomsManager;
import ar.com.dcsys.model.filters.FiltersProcessor;
import ar.com.dcsys.model.filters.PropertyAccesor;
import ar.com.dcsys.model.filters.ReserveAttemptDateFilter;
import ar.com.dcsys.model.filters.types.ClassRoomFilter;
import ar.com.dcsys.model.filters.types.CourseFilter;
import ar.com.dcsys.model.filters.types.PersonFilter;
import ar.com.dcsys.model.filters.types.StartEndDateFilter;
import ar.com.dcsys.model.silabouse.CoursesManager;
import ar.com.dcsys.model.silabouse.UntouchableSubjectsManager;


@Singleton
public class ReserveAttemptsManagerBean implements ReserveAttemptsManager {

	private static Logger logger = Logger.getLogger(ReserveAttemptsManagerBean.class.getName());
	
	private final ReserveAttemptDAO reserveAttemptDAO;
	private final ReserveAttemptDateDAO reserveAttemptDateDAO;

	private final ClassRoomsManager classRoomsManager;
	private final CoursesManager coursesManager;
	private final ReservesManager reservesManager;
	private final PersonsManager personsManager;
	private final UntouchableSubjectsManager untouchableSubjectsManager;

	
	private CoursesForPerson[] coursesForPersons;		// para analizar los permisos de administración y visión de cursos.
	private FilterConverter[] filterConverters;			// para convertir los filtros enum en formato de transferencia a filtros aplicables en el modelo.
	
	@Inject
	public ReserveAttemptsManagerBean(ReserveAttemptDAO reserveAttemptDAO, ReserveAttemptDateDAO reserveAttemptDateDAO, 
									  ClassRoomsManager classRoomsManager, CoursesManager coursesManager, ReservesManager reservesManager,
									  PersonsManager personsManager, UntouchableSubjectsManager untouchableSubjectsManager) {

		this.reserveAttemptDAO = reserveAttemptDAO;  
		this.reserveAttemptDateDAO = reserveAttemptDateDAO;
		
		this.classRoomsManager = classRoomsManager;
		this.coursesManager = coursesManager;
		this.reservesManager = reservesManager;
		this.personsManager = personsManager;
		this.untouchableSubjectsManager = untouchableSubjectsManager;
		
		coursesForPersons = new CoursesForPerson[2];
	//	coursesForPersons[0] = new AdminCourses(authsManager, personsManager, groupsManager, aManager);
	//	coursesForPersons[1] = new TeacherCourses(assignmentsManager, authsManager, personsManager);
		
		
		filterConverters = new FilterConverter[4];
		filterConverters[0] = new CourseFilterC();
		filterConverters[1] = new ClassRoomFilterC();
		filterConverters[2] = new PersonFilterC();
		filterConverters[3] = new StartEndDateFilterC();
			
		createCaches();
			
	}
	
	private void createCaches() {
		
	}
	
	/*
	 * RESERVE ATTEMPT DATE
	 */
	@Override
	public String persist(ReserveAttemptDate date) throws MapauException {
		return reserveAttemptDateDAO.persist(date);
	}
	
	@Override
	public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException {
		try {
			return reserveAttemptDateDAO.findById(id);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	/**
	 * Encuentra las reserveattemptDate entre las fechas pasadas como parámetro.
	 * los reserveattmptdate retornados son los ultimos de la cadena de modificaciones de reserveattemptdate.
	 * o sea los que tienen el reserveattemptdate_id == null!!!. (son los ultimos de la lista).
	 */
	@Override
	public List<ReserveAttemptDate> findReserveAttemptDateBy(Date start, Date end) throws MapauException {
		try {
			return reserveAttemptDateDAO.findBy(start, end);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	
	
	
	/*
	 * RESERVE ATTEMPT
	 */
	
	@Override
	public ReserveAttemptDeleted findReserveAttemptDeletedById(String id) throws MapauException {
		try {
			return reserveAttemptDAO.findReserveAttemptDeletedById(id);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public String persist(ReserveAttemptDeleted rd) throws MapauException {
		return reserveAttemptDAO.persist(rd);
	}
	
	@Override
	public List<ReserveAttemptDate> checkDateAvailable(List<ReserveAttemptDate> date) throws MapauException {
		return null;
	}

	@Override
	public Boolean checkDateAvailable(ReserveAttemptDate date) throws MapauException {
		return null;
	}

	@Override
	public void removeVisible(ReserveAttemptDate rad) throws MapauException {

	}
	
	@Override
	public void setVisible(ReserveAttemptDate rad) throws MapauException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setVisible(ReserveAttemptDate rad, List<Group> groups) throws MapauException {
		// TODO Auto-generated method stub
		
	}
	
	/** 
	 * Metodo que obtiene las catedras que pude ver la persona que se loquea en 
	 * las pantallas para crear reserva
	 * chequea los courses groups que existen y los grupos de la persona. asi determina que catedras
	 * */
	@Override
	public List<Course> getCoursesToCreateReserveAttempt() throws MapauException {
		try {
			List<Course> courses = new ArrayList<>();
			for (CoursesForPerson sfp : coursesForPersons) {
				courses.addAll(sfp.getCourses());
			}
			return courses;
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	
	/**
	 * Obtiene los reserve attempts que puede confirmar la persona logueada dentro del sistema.
	 * se obtienen los grupos del usuario logueado para asi obtener las areas relacionadas y filtrar los 
	 * reserve attempts por area
	 * Una vez filtrados los reserveAttempts de eso se retorna los reserve Attemps que no esten completamente confirmados.
	 */
	@Override
	public List<ReserveAttempt> getReserveAttemptsToConfirmReserve() throws MapauException {
		throw new MapauException("no implementado");
		/*
		try {
			List<Area> areas = AreasUtilsBean.getAreasFromLoggedUser(authsManager,personsManager,groupsManager,aManager);

			List<ReserveAttempt> lra = new ArrayList<ReserveAttempt>();

			for (Area sg : areas) {
				if (sg != null && sg.getId() != null) {
					lra.addAll(reserveAttemptManager.findStandingReserveAttemptsBy(sg));
				}
			}
			return lra;
			
		} catch (AuthException | PersonException e) {
			throw new MapauException(e);
		}
		*/
	}


	/////// filters //////////////
	
	private interface FilterConverter {
		public ReserveAttemptDateFilter getFilter(TransferFilter tf) throws FilterException;
	}
	
	private class ClassRoomFilterC implements FilterConverter {
		@Override
		public ReserveAttemptDateFilter getFilter(TransferFilter tf) throws FilterException {
			
			if (!tf.getType().equals(TransferFilterType.CLASSROOM)) {
				return null;
			}

			try {
				
				// busco el classRoom 
				String classRoomId = tf.getParam();
				ClassRoom classRoom = classRoomsManager.findById(classRoomId);
				if (classRoom == null) {
					throw new FilterException("No se encuentra el aula con el id = " + classRoomId);
				}
				
				PropertyAccesor<ClassRoom> pc = new PropertyAccesor<ClassRoom>() {
					@Override
					public boolean multipleValues() {
						return true;
					}
	
					@Override
					public List<ClassRoom> getProperties(ReserveAttemptDate ra) throws FilterException {
						try {
							List<Reserve> reserves = reservesManager.findBy(ra);
							if (reserves == null) {
								return null;
							}
							
							List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
							for (Reserve r : reserves) {
								classRooms.add(r.getClassRoom());
							}
							return classRooms;
							
						} catch (MapauException e) {
							throw new FilterException(e);
						}
					}
					
					@Override
					public ClassRoom getProperty(ReserveAttemptDate ra)	throws FilterException {
						throw new FilterException("multiple values posibles");
					}
				};
				
				ClassRoomFilter cf = new ClassRoomFilter(classRoom, pc);
				return cf;
					
			} catch (MapauException e) {
				throw new FilterException(e);
			}
		}
	}
	
	private class CourseFilterC implements FilterConverter {
		@Override
		public ReserveAttemptDateFilter getFilter(TransferFilter tf) throws FilterException {
			
			if (!tf.getType().equals(TransferFilterType.COURSE)) {
				return null;
			}
			
			try {
				// busco el curso.
				String courseId = tf.getParam();
				Course course = coursesManager.findById(courseId);
				if (course == null) {
					throw new FilterException("No se encuentra curso con id = " + courseId);
				}
				
				CourseFilter cf = new CourseFilter(course);
				return cf;
				
			} catch (MapauException e) {
				throw new FilterException(e);
			}
		}
	}
	
	private class PersonFilterC implements FilterConverter {
		@Override
		public ReserveAttemptDateFilter getFilter(TransferFilter tf) throws FilterException {
			
			if (!tf.getType().equals(TransferFilterType.PERSON)) {
				return null;
			}
			
			try {
				// busco la persona.
				String personId = tf.getParam();
				Person person = personsManager.findById(personId);
				if (person == null) {
					throw new FilterException("No se encuentra la persona con id = " + personId);
				}
				
				PersonFilter cf = new PersonFilter(person);
				return cf;
				
			} catch (PersonException e) {
				throw new FilterException(e);
			}
		}
	}
	
	/**
	 * Como quede con ema el formato es : long;long
	 * @author pablo
	 *
	 */
	private class StartEndDateFilterC implements FilterConverter {
		@Override
		public ReserveAttemptDateFilter getFilter(TransferFilter tf) throws FilterException {

			Date start = null;
			Date end = null;
			
			String param = tf.getParam();
			
			try {
				StringTokenizer st = new StringTokenizer(param,";");
				if (st.hasMoreTokens()) {
					String sstart = st.nextToken();
					long lstart = Long.valueOf(sstart);
					start = new Date(lstart);  
				}
				if (st.hasMoreTokens()) {
					String send = st.nextToken();
					long lend = Long.valueOf(send);
					end = new Date(lend);  
				}
			} catch (Exception e) {
				throw new FilterException(e);
			}
			
			if (start == null || end == null) {
				throw new FilterException("No se pudieron parsear correctamente las fechas desde : " + param);
			}
			
			return new StartEndDateFilter(start,end);
		}
	}
	
	
	/**
	 * Convierte desde el formato de transferencia al formato interno de los filtros.
	 * para despues ser aplicados a alguna búsqueda.
	 * @param tf
	 * @return
	 * @throws FilterException
	 */
	private ReserveAttemptDateFilter convert(TransferFilter tf) throws FilterException {
		if (tf == null) {
			throw new FilterException("TransferFilter == null");
		}
		
		if (tf.getType() == null) {
			throw new FilterException("TransferFilter.type == null");
		}
		
		if (tf.getParam() == null) {
			throw new FilterException("TransferFilter.param == null");
		}
		
		if (filterConverters == null || filterConverters.length == 0) {
			throw new FilterException("filterConverters == null");	// error en el @PostConstruct
		}

		ReserveAttemptDateFilter radf = null;
		for (FilterConverter fc : filterConverters) {
			radf = fc.getFilter(tf);
			if (radf != null) {
				return radf;
			}
		}
		
		throw new FilterException("No se pudo convertir " + tf.getType());
	}
	
	@Override
	public List<TransferFilterType> findAllFilters() throws MapauException {
		return Arrays.asList(TransferFilterType.values());
	}
	

	
	/**
	 * Obtiene los ReserveAttemptDate filtrados por los filtros pasados por parámetro.
	 */
	@Override
	public List<ReserveAttemptDate> findReserveAttemptDateBy(Date start, Date end, List<TransferFilter> filters) throws MapauException {
		
		if (filters == null || filters.size() <= 0) {
			return findReserveAttemptDateBy(start, end);
		}
		
		List<ReserveAttemptDate> rads = findReserveAttemptDateBy(start, end);
		if (rads == null || rads.size() <= 0) {
			return rads;
		}
		
		try {
			
			// obtengo los filtros del modelo representados por el parámetro filters.
			
			List<ReserveAttemptDateFilter> rfilters = new ArrayList<>(filters.size());
			for (TransferFilter tf : filters) {
				ReserveAttemptDateFilter radf = convert(tf);
				rfilters.add(radf);
			}

			// filtro los reserve attempt dates de acuerdo a los filtros obtenidos en la conversion.
			List<ReserveAttemptDate> filtered = FiltersProcessor.filter(rfilters, rads);

			return filtered;
		
		} catch (FilterException e) {
			throw new MapauException(e);
		}
	}
	
	
	
	
	
	
	/**
	 * Implementa un shallowCopy del appointment.
	 * @param a
	 * @return
	 */
	private Appointment shallowCopy(Appointment a) {
		Appointment a2 = new AppointmentBean();
		a2.setAreaId(a.getAreaId());
		a2.setClassRoomId(a.getClassRoomId());
		a2.setClassRoomName(a.getClassRoomName());
		a2.setCourseId(a.getCourseId());
		a2.setCourseName(a.getCourseName());
		a2.setDescription(a.getDescription());
		a2.setEnd(a.getEnd());
		a2.setStart(a.getStart());
		a2.setId(a.getId());
		a2.setRadId(a.getRadId());
		a2.setRaId(a.getRaId());
		a2.setRaTypeId(a.getRaTypeId());
		a2.setRaTypeName(a.getRaTypeName());
		a2.setRelatedAppointments(a.getRelatedAppointments());
		a2.setRelatedPersonsIds(a.getRelatedPersonsIds());
		a2.setrId(a.getrId());
		a2.setStudentGroup(a.getStudentGroup());
		a2.setVisible(a.getVisible());
		return a2;
	}
	
	
	/**
	 * Implementa un shallowCopy del appointment.
	 * @param a
	 * @return
	 */
	private AppointmentV2 shallowCopy(AppointmentV2 a) {
		AppointmentV2 a2 = new AppointmentV2Bean();
		a2.setArea(a.getArea());
		a2.setClassRoom(a.getClassRoom());
		a2.setCourse(a.getCourse());
		a2.setDescription(a.getDescription());
		a2.setEnd(a.getEnd());
		a2.setStart(a.getStart());
		a2.setId(a.getId());
		a2.setRad(a.getRad());
		a2.setRaType(a.getRaType());
		a2.setRelatedAppointments(a.getRelatedAppointments());
		if (a.getRelatedPersons() != null) {
			a2.setRelatedPersons(new ArrayList<Person>(a.getRelatedPersons()));
		}
		if (a.getCharacteristics() != null) {
			a2.setCharacteristics(new ArrayList<CharacteristicQuantity>(a.getCharacteristics()));
		}
		a2.setR(a.getR());
		a2.setStudentGroup(a.getStudentGroup());
		a2.setVisible(a.getVisible());
		return a2;
	}	
	
	/**
	 * Obtegno los valores del ReserveAttemptDate y lo transformo en un Appointment para enviarlo al cliente.
	 * En la generación se tiene en cuenta ls ReserveAttempt y los Reserves que tenga
	 * Cada reserve genera un Appointment indifvidual pero que tiene el mismo RaId y RadId
	 * @param rad
	 * @return
	 * @throws MapauException
	 */
	private List<Appointment> toAppointment(ReserveAttemptDate rad) throws MapauException {
		if (rad == null) {
			throw new MapauException("reserveAttemptDate == null");
		}

		String areaId = rad.getArea().getId();
		String courseId = rad.getCourse().getId();
		String courseName = rad.getCourse().getName();
		String raTypeId = rad.getType().getId();
		String raTypeName = rad.getType().getName();
		String studentGroup = rad.getStudentGroup();
		String description = rad.getDescription();
		List<Person> relatedPersons = rad.getRelatedPersons();

		String radId = rad.getId();
		Date start = rad.getStart();
		Date end = rad.getEnd();
		
		Appointment a = new AppointmentBean();
		a.setRadId(radId);
		a.setAreaId(areaId);
		a.setCourseId(courseId);
		a.setCourseName(courseName);
		a.setEnd(end);
		a.setStart(start);
		a.setRaTypeId(raTypeId);
		a.setRaTypeName(raTypeName);
		a.setStudentGroup(studentGroup);
		a.setDescription(description);
		
		List<String> relatedPersonsIds = new ArrayList<>();
		for (Person p : relatedPersons) {
			relatedPersonsIds.add(p.getId());
		}
		a.setRelatedPersonsIds(relatedPersonsIds);		
		
		// por defecto no tiene reserva realizada.
		a.setrId(null);
		a.setClassRoomId(null);
		a.setClassRoomName(null);

		// muto el appointment de acuerdo a las reservas que tenga realizada.
		
		List<Appointment> aps = new ArrayList<>();
		List<Reserve> reserves = reservesManager.findBy(rad);

		if (reserves == null || reserves.size() <= 0) {
			// solo retorno 1 solo appointment.
			aps.add(a);
			
		} else {
			
			for (Reserve r : reserves) {

				// chequeo por seguridad solamente que sea el ultimo reserve. o sea la reserva actual.
				// siempre deberían ser pero por las dudas lo cheuqeo aca también.
				if (r.getRelated() != null) {
					continue;
				}
				
				Appointment a2 = shallowCopy(a);
				
				description = (r.getDescription() != null) ? r.getDescription() : description;
				a2.setDescription(description);

				if (r.getRelatedPersons() != null && r.getRelatedPersons().size() > 0) {
					
					relatedPersons = r.getRelatedPersons();
					relatedPersonsIds.clear();
					
					for (Person p : relatedPersons) {
						relatedPersonsIds.add(p.getId());
					}
					a2.setRelatedPersonsIds(relatedPersonsIds);
					
				}

				String classRoomId = r.getClassRoom().getId();
				String classRoomName = r.getClassRoom().getName();
				a2.setClassRoomId(classRoomId);
				a2.setClassRoomName(classRoomName);
				
				String rId = r.getId();
				a2.setrId(rId);
				
				aps.add(a);
			}
		}
		
		return aps;
	}
	
	
	/**
	 * Obtegno los valores del ReserveAttemptDate y lo transformo en un Appointment para enviarlo al cliente.
	 * En la generación se tiene en cuenta ls ReserveAttempt y los Reserves que tenga
	 * Cada reserve genera un Appointment indifvidual pero que tiene el mismo RaId y RadId
	 * @param rad
	 * @return
	 * @throws MapauException
	 */
	private List<AppointmentV2> toAppointmentV2(ReserveAttemptDate rad) throws MapauException {
		if (rad == null) {
			throw new MapauException("reserveAttemptDate == null");
		}
		
		AppointmentV2 a = new AppointmentV2Bean();
		a.setRad(rad);
		a.setStart(rad.getStart());
		a.setEnd(rad.getEnd());
		a.setArea(rad.getArea());
		a.setCourse(rad.getCourse());
		a.setRaType(rad.getType());
		a.setStudentGroup(rad.getStudentGroup());
		a.setDescription(rad.getDescription());
		a.setRelatedPersons(rad.getRelatedPersons());
		a.setCharacteristics(rad.getCharacteristicsQuantity());
		
		// por defecto no tiene reserva realizada.
		a.setR(null);
		a.setClassRoom(null);

		// muto el appointment de acuerdo a las reservas que tenga realizada.
		
		List<AppointmentV2> aps = new ArrayList<>();
		List<Reserve> reserves = reservesManager.findBy(rad);

		if (reserves == null || reserves.size() <= 0) {
			// solo retorno 1 solo appointment.
			aps.add(a);
			
		} else {
			
			for (Reserve r : reserves) {

				// chequeo por seguridad solamente que sea el ultimo reserve. o sea la reserva actual.
				// siempre deberían ser pero por las dudas lo cheuqeo aca también.
				if (r.getRelated() != null) {
					continue;
				}
				
				AppointmentV2 a2 = shallowCopy(a);

				String description = a2.getDescription();
				description = (r.getDescription() != null) ? r.getDescription() : description;
				a2.setDescription(description);


				if (r.getRelatedPersons() != null && r.getRelatedPersons().size() > 0) {
					a2.setRelatedPersons(r.getRelatedPersons());
				}

				a2.setClassRoom(r.getClassRoom());
				a2.setR(r);
				
				aps.add(a2);
			}
		}
		
		return aps;
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Genero ids y los seteo uno por acada appointment.
	 * tambien seteo los appointments relacionados. esto es todos los otros ids menos el propio.
	 * @param aps
	 */
	private void generateRelations(List<Appointment> aps) {
		
		long seed = (new Date()).getTime();
		Random r = new Random(seed);

		List<String> relatedIds = new ArrayList<>(aps.size());
		for (int i = 0; i < aps.size(); i++) {
			relatedIds.add(String.valueOf(r.nextInt()));
		}
		
		for (int i = 0; i < aps.size(); i++) {
			Appointment a = aps.get(i);
			
			String id = relatedIds.get(i);
			List<String> relatedApp = new ArrayList<>(relatedIds);
			relatedApp.remove(id);
			
			a.setId(id);
			a.setRelatedAppointments(relatedApp);
		}
	}
	
	/**
	 * Retorno todos los appointmetns determinados por los ReserveAttempts obtenidos entre las fechas y filtros inidcados.
	 */
	@Override
	public List<Appointment> findAppointmentsBy(Date start, Date end, List<TransferFilter> filters) throws MapauException {
		
		if (filters == null) {
			throw new MapauException("filters == null");
		}
		
		List<Appointment> aps = new ArrayList<>();
		
		List<ReserveAttemptDate> rads = findReserveAttemptDateBy(start, end, filters);
		if (rads == null || rads.size() <= 0) {
			return aps;
		}
		
		for (ReserveAttemptDate rad : rads) {
			List<Appointment> ap = toAppointment(rad);
			aps.addAll(ap);
		}
		
		generateRelations(aps);
		
		return aps;
	}
	
	/**
	 * Retorno todos los appointmetns determinados por los ReserveAttempts obtenidos entre las fechas y filtros inidcados.
	 */
	public List<AppointmentV2> findAppointmentsV2By(Date start, Date end, List<TransferFilter> filters) throws MapauException {
		
		if (filters == null) {
			throw new MapauException("filters == null");
		}
		
		List<AppointmentV2> aps = new ArrayList<>();
		
		List<ReserveAttemptDate> rads = findReserveAttemptDateBy(start, end, filters);
		if (rads == null || rads.size() <= 0) {
			return aps;
		}
		
		for (ReserveAttemptDate rad : rads) {
			List<AppointmentV2> ap = toAppointmentV2(rad);
			aps.addAll(ap);
		}
		
		// despues lo implemento si es necesario
		//generateRelations(aps);
		
		return aps;
	}	
	
	/**
	 * Obtiene los appointments definidos por los filtros.
	 * Chequea que exista siempre un filtro de tipo TransferFilterType.DATE.
	 * asi evita cruzar toda la base en la consulta al backend.
	 */
	@Override
	public List<Appointment> findAppointmentsBy(List<TransferFilter> filters) throws MapauException {
		
		if (filters == null) {
			throw new MapauException("filters == null");
		}
		
		// para no cruzar toda la base chequeo que tegna un filtro de tipo fecha. 
		// SIEMPRE tiene que estar ese filtro.

		try {
		
			StartEndDateFilter rf = null;
			
			Iterator<TransferFilter> it = filters.iterator();
			while (it.hasNext()) {
				TransferFilter tf = it.next();
				if (TransferFilterType.DATE.equals(tf.getType())) {
					it.remove();		// no se pasa a procesar en el arbol de filtros.
					rf = (StartEndDateFilter)convert(tf);
					break;
				}
			}
			
			if (rf == null) {
				throw new MapauException("No se definió ningún TransferFilterType.DATE");
			}
		
			return findAppointmentsBy(rf.getStart(),rf.getEnd(),filters);
			
		} catch (FilterException e) {
			throw new MapauException(e);
		}
	}
	
	/**
	 * Obtiene los appointments definidos por los filtros.
	 * Chequea que exista siempre un filtro de tipo TransferFilterType.DATE.
	 * asi evita cruzar toda la base en la consulta al backend.
	 */
	@Override
	public List<AppointmentV2> findAppointmentsV2By(List<TransferFilter> filters) throws MapauException {
		
		if (filters == null) {
			throw new MapauException("filters == null");
		}
		
		// para no cruzar toda la base chequeo que tegna un filtro de tipo fecha. 
		// SIEMPRE tiene que estar ese filtro.

		try {
		
			StartEndDateFilter rf = null;
			
			Iterator<TransferFilter> it = filters.iterator();
			while (it.hasNext()) {
				TransferFilter tf = it.next();
				if (TransferFilterType.DATE.equals(tf.getType())) {
					it.remove();		// no se pasa a procesar en el arbol de filtros.
					rf = (StartEndDateFilter)convert(tf);
					break;
				}
			}
			
			if (rf == null) {
				throw new MapauException("No se definió ningún TransferFilterType.DATE");
			}
		
			return findAppointmentsV2By(rf.getStart(),rf.getEnd(),filters);
			
		} catch (FilterException e) {
			throw new MapauException(e);
		}
	}	
	
	
	/**
	 * Chequea que las listas contengan los mismos ids. no necesariamente en el mismo orden.
	 * @param ids
	 * @param ids2
	 * @return
	 */
	private boolean checkIds(List<String> ids, List<String> ids2) {
		if (ids == null || ids2 == null) {
			return false;
		}
		return (ids.containsAll(ids2) && ids2.containsAll(ids));
	}
	
	/**
	 * Elimina las reservas que tenga un appointment en particular.
	 * @param appointmnet
	 * @throws MapauException
	 */
	public void deleteReserves(Appointment a) throws MapauException, PersonException {
		
		if (a == null) {
			throw new MapauException("appointment == null");
		}
		
		String radId = a.getRadId();
		if (radId == null) {
			throw new MapauException("appointment.ReserveAttemptDateId == null");
		}
		
		
		ReserveAttemptDate rad = reserveAttemptDateDAO.findById(radId);
		if (rad == null) {
			throw new MapauException("ReserveAttemptDate == not existent");
		}
		
		List<Reserve> reserves = reservesManager.findBy(rad);
		if (reserves == null || reserves.size() <= 0) {
			return;
		}
		
		for (Reserve r : reserves) {
			reservesManager.remove(r);
		}
		
	}
	
	/**
	 * Crea una nueva reserva de aula/s
	 * En el caso de tener ya reservas asignadas NO permite realizar la asignación. 
	 * Debe eliminarse anteriormente cualquier reserva que tenga el apointment.
	 * 
	 * @param appointment
	 * @param classRoom
	 * @throws MapauException
	 */
	public void createReserve(Appointment a, List<ClassRoom> classRooms) throws MapauException, PersonException {
		if (a == null) {
			throw new MapauException("appointment == null");
		}
		
		String radId = a.getRadId();
		if (radId == null) {
			throw new MapauException("appointment.ReserveAttemptDateId == null");
		}
		
		if (classRooms == null || classRooms.size() <= 0) {
			throw new MapauException("ClassRooms == null || size <= 0");
		}
		
		ReserveAttemptDate rad = reserveAttemptDateDAO.findById(radId);
		if (rad == null) {
			throw new MapauException("ReserveAttemptDate == not existent");
		}
		
		List<Reserve> reserves = reservesManager.findBy(rad);
		if (reserves != null && reserves.size() > 0) {
			throw new MapauException("La fecha ya tiene reservas asignadas, debe eliminarlas antes de poder realizar una nueva");
		}
		
		/* Comentado para que compile 3/12
		DCSysPrincipal principal = authsManager.getUserPrincipal();
		Person owner = personsManager.findByPrincipal(principal);				
		*/
		for (ClassRoom c : classRooms) {
			Reserve r = new ReserveBean();
			r.setClassRoom(c);
		//	r.setOwner(owner);
			r.setCreated(new Date());
			r.setReserveAttemptDate(rad);
			reservesManager.persist(r);
		}
	}
	
	
	/**
	 * Modifica el appointment pasado como parámetro.
	 * Para poder modificar el horario/fecha de un appointment NO debe tener reservas realizadas.
	 * NO!!! se puede modificar el classRoom mediante este método. Eso se puede hacer reservando un aula para determinado apointment.
	 * @param appointments
	 * @throws MapauException
	 */
	public void modify(AppointmentV2 a) throws MapauException, PersonException {
		
		ReserveAttemptDate rad = a.getRad();
		if (rad == null) {
			throw new MapauException("appointment.rad == null");
		}
		
		String description = a.getDescription();
		Date end = a.getEnd();
		Date start = a.getStart();
		Course course = a.getCourse();
		ReserveAttemptDateType type = a.getRaType();
		Area area = a.getArea();
		String studentGroup = a.getStudentGroup();
		List<Person> relatedPersons = a.getRelatedPersons();
		List<CharacteristicQuantity> characteristics = a.getCharacteristics();
		
		if (!(start.equals(rad.getStart())) || (!(end.equals(rad.getEnd())))) {
			// fechas diferentes NO DEBE TENER RESERVAS REALIZADAS.
			List<Reserve> reserves = reservesManager.findBy(rad);		
			if (reserves != null && reserves.size() > 0) {
				throw new MapauException("No se puede modificar una reserva que tenga aula asignada");
			}
		}
		
		/*
		 * Comentado 3/12
		 */
		/*DCSysPrincipal principal = authsManager.getUserPrincipal();
		Person owner = personsManager.findByPrincipal(principal);	*/	
		
		ReserveAttemptDate modifiedRad = rad;
		modifiedRad.setCreationDate(new Date());
		//modifiedRad.setCreator(owner);
		modifiedRad.setDescription(description);
		modifiedRad.setEnd(end);
		modifiedRad.setStart(start);
		modifiedRad.setRelated(null);
		modifiedRad.setArea(area);
		modifiedRad.setCourse(course);
		modifiedRad.setType(type);
		modifiedRad.setStudentGroup(studentGroup);
		modifiedRad.setRelatedPersons(relatedPersons);
		modifiedRad.setCharacteristicsQuantity(characteristics);
		String newId = persist(modifiedRad);
		
	}	
	
	
	/**
	 * Modifica el appointment pasado como parámetro.
	 * Para poder modificar el horario/fecha de un appointment NO debe tener reservas realizadas.
	 * NO!!! se puede modificar el classRoom mediante este método. Eso se puede hacer reservando un aula para determinado apointment.
	 * @param appointments
	 * @throws MapauException
	 */
	/*
	public void modify(Appointment a) throws MapauException, PersonException, AuthException {
		
		String raId = a.getRaId();
		if (raId == null) {
			throw new MapauException("appointment.raId == null");
		}
		
		String radId = a.getRadId();
		if (radId == null) {
			throw new MapauException("appointment.radId == null");
		}
		
		ReserveAttemptDate rad = reserveAttemptManager.findReserveAttemptDateById(radId);
		if (rad == null) {
			throw new MapauException("ReserveAttemptDate not found");
		}
		
		ReserveAttempt ra = rad.getReserveAttempt();
		
		String courseId = a.getCourseId();
		if (!(courseId.equals(rad.getCourse().getId()))) {
			throw new MapauException("ReserveAttempt.Course no se puede modificar");
		}
		
		String studentGroup = a.getStudentGroup();
		if (!(studentGroup.equals(rad.getStudentGroup()))) {
			throw new MapauException("ReserveAttempt.StudentGroup no se puede modificar");
		}
		
		
		// parámetros modificables de los reserveAttemptDate
		
		String description = a.getDescription();
		Date end = a.getEnd();
		Date start = a.getStart();
		
		if (!(start.equals(rad.getStart())) || (!(end.equals(rad.getEnd())))) {
			
			// fechas diferentes NO DEBE TENER RESERVAS REALIZADAS.
			List<Reserve> reserves = reservesManager.findBy(rad);		
			if (reserves != null && reserves.size() > 0) {
				throw new MapauException("No se puede modificar el/la fecha/horario de un pedido si ya tiene reserva");
			}
		
			DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person owner = personsManager.findByPrincipal(principal);		
			
			ReserveAttemptDate modifiedRad = new ReserveAttemptDate();
			modifiedRad.setCreationDate(new Date());
			modifiedRad.setCreator(owner);
			modifiedRad.setDescription(description);
			modifiedRad.setEnd(end);
			modifiedRad.setStart(start);
			modifiedRad.setRelated(null);
			modifiedRad.setReserveAttempt(ra);
			
			String newId = persist(modifiedRad);
			ReserveAttemptDate mrad = findReserveAttemptDateById(newId);
			
			rad.setRelated(mrad);
			persist(rad);
		
		} else {
			// fechas iguales.
			
			// se modifica la descripción?
			if (description != null &&
			   ((ra.getDescription() != null && (!(description.equals(ra.getDescription())))) ||
			   ((rad.getDescription() != null && (!(description.equals(rad.getDescription()))))))) {
				   
				rad.setDescription(description);
				persist(rad);
				
			}
		}
	}
	*/
	
	/**
	 * Crea un nueov pedido de reserva determinado por los datos de los appointments parados por parámetro.
	 */
	/*
	@Override
	public void createNew(List<Appointment> appointments) throws MapauException {

		if (appointments == null || appointments.size() <= 0) {
			throw new MapauException("appointments == null");
		}
		
		Appointment a = appointments.get(0);
		String courseId = a.getCourseId();
		String studentGroup = a.getStudentGroup();
		String areaId = a.getAreaId();
		String typeId = a.getRaTypeId();
		List<String> personsIds = a.getRelatedPersonsIds();
		
		// chequeo las precondiciones.
		
		for (Appointment ap : appointments) {
			if (!(ap.getCourseId() != null && ap.getCourseId().equals(courseId))) {
				throw new MapauException("courseId == null || courseId diferentes");
			}
			
			if (!(ap.getStudentGroup() != null && ap.getStudentGroup().equals(studentGroup))) {
				throw new MapauException("studentGroup == null || studentGroup diferentes");
			}
			
			if (ap.getStart() == null || ap.getEnd() == null) {
				throw new MapauException("appointment.date == null");
			}
			
			if (!(ap.getAreaId() != null && ap.getAreaId().equals(areaId))) {
				throw new MapauException("appointment.areaId == null || appointment.areaId diferentes");
			}
			
			if (!(ap.getRaTypeId() != null && ap.getRaTypeId().equals(typeId))) {
				throw new MapauException("appointment.raTypeId == null || appointment.raTypeId diferentes");
			}
			
			if (!checkIds(personsIds, ap.getRelatedPersonsIds())) {
				throw new MapauException("appointments.relatedPersons diferentes");
			}
		}
		
		// creo el reserveattmpt y los reserveattemptdate
		
		try {
			DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person owner = personsManager.findByPrincipal(principal);
		
			Course course = coursesManager.findById(courseId);
			Area area = aManager.findById(areaId);
			ReserveAttemptDateType type = ratManager.findById(typeId);
			
			List<Person> persons = new ArrayList<>();
			for (String pId : personsIds) {
				Person person = personsManager.findById(pId);
				persons.add(person);
			}
			
			ReserveAttempt ra = new ReserveAttempt();
			ra.setOwner(owner);
			ra.setCreated(new Date());
			ra.setCourse(course);
			ra.setStudentGroup(studentGroup);
			ra.setArea(area);
			ra.setType(type);
			ra.setRelatedPersons(persons);
			
			List<ReserveAttemptDate> rads = new ArrayList<>();
			for (Appointment ap : appointments) {

				String description = ap.getDescription();
				ReserveAttemptDate rad = new ReserveAttemptDate();
				rad.setCreationDate(new Date());
				rad.setCreator(owner);
				rad.setDescription(description);
				rad.setStart(ap.getStart());
				rad.setEnd(ap.getEnd());
				rad.setReserveAttempt(ra);
				
				rads.add(rad);
			}

			persist(ra, rads);
			
		} catch (AuthException e) {
			throw new MapauException(e);
			
		} catch (PersonException e) {
			throw new MapauException(e);
			
		} catch (SilegException e) {
			throw new MapauException(e);
		}
	}
	
	*/
	
	
	/**
	 * Crea un nueov pedido de reserva determinado por los datos de los appointments parados por parámetro.
	 */
	@Override
	public void createNewAppointments(List<AppointmentV2> appointments) throws MapauException {

		if (appointments == null || appointments.size() <= 0) {
			throw new MapauException("appointments == null");
		}
		
		// creo el reserveattmpt y los reserveattemptdate

			/*
			 * Comentado para que compile 3/12
			 */
			/*DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person owner = personsManager.findByPrincipal(principal);*/
			
			List<ReserveAttemptDate> rads = new ArrayList<>();
			for (AppointmentV2 ap : appointments) {

				String description = ap.getDescription();
				ReserveAttemptDate rad = new ReserveAttemptDateBean();
				rad.setCreationDate(new Date());
				//rad.setCreator(owner);
				rad.setDescription(description);
				rad.setStart(ap.getStart());
				rad.setEnd(ap.getEnd());
				rad.setCourse(ap.getCourse());
				rad.setStudentGroup(ap.getStudentGroup());
				rad.setArea(ap.getArea());
				rad.setType(ap.getRaType());
				
				List<Person> relatedPersons = ap.getRelatedPersons();
				if (relatedPersons != null && relatedPersons.size() > 0) {
					rad.getRelatedPersons().addAll(relatedPersons);
				}
				
				List<CharacteristicQuantity> chars = ap.getCharacteristics();
				if (chars != null && chars.size() > 0) {
					rad.getCharacteristicsQuantity().addAll(chars);
				}
				
				rads.add(rad);
				
				persist(rad);
			}
			

				
	}
	
	@Override
	public void deleteAppointment(AppointmentV2 app) throws MapauException {
		
		if (app == null) {
			throw new MapauException("reserva == null");
		}
		
		ReserveAttemptDate rad = app.getRad();
		if (rad == null) {
			throw new MapauException("reserva.reserveattemptdate == null");
		}
		
		reserveAttemptDateDAO.remove(rad);
	}
	
	
	
	/////////// CLASSROOMS /////////////////

	@Override
	public List<AppointmentV2> findAllAppointmentsBy(AppointmentV2 app, List<Date> dates, boolean checkHour) throws MapauException {
		ReserveAttemptDate rad = app.getRad();
		List<ReserveAttemptDate> rads = findAllAppointmentsBy(rad, dates, checkHour);
		List<AppointmentV2> appointments = new ArrayList<AppointmentV2>();
		for (ReserveAttemptDate r : rads) {
			List<AppointmentV2> app2 = toAppointmentV2(r);
			appointments.addAll(app2);
		}
		return appointments;
	};
	
	
	/**
	 * Encuentra todos los appointmnets que matchean con las condiciones pasadas como parámetro.
	 */
	@Override
	public List<ReserveAttemptDate> findAllAppointmentsBy(ReserveAttemptDate app, List<Date> dates, boolean checkHour) throws MapauException {
		
		if (app == null) {
			throw new MapauException("app == null");
		}
		
		List<ReserveAttemptDate> rads = new ArrayList<ReserveAttemptDate>();
		
		if (dates == null || dates.size() <= 0)  {
			return rads;
		}
		
		try {
		
			List<DatesRange> rdates = new ArrayList<DatesRange>();
			
			if (checkHour) {
				
				// tengo que ajustar las fechas para que sean iguales a las del Appointment pasado como parámetro.
				
				Date start = app.getStart();
				Date end = app.getEnd();
				Calendar c = Calendar.getInstance();
				
				c.setTime(start);
				int shour = c.get(Calendar.HOUR_OF_DAY);
				int sminute = c.get(Calendar.MINUTE);
				int ssecond = c.get(Calendar.SECOND);
				int smilis = c.get(Calendar.MILLISECOND);
				
				c.setTime(end);
				int ehour = c.get(Calendar.HOUR_OF_DAY);
				int eminute = c.get(Calendar.MINUTE);
				int esecond = c.get(Calendar.SECOND);
				int emilis = c.get(Calendar.MILLISECOND);
				
				
				for (Date date : dates) {
					c.setTime(date);
					c.set(Calendar.HOUR_OF_DAY,shour);
					c.set(Calendar.MINUTE,sminute);
					c.set(Calendar.SECOND,ssecond);
					c.set(Calendar.MILLISECOND,smilis);
					Date newStart = c.getTime();
					
					c.set(Calendar.HOUR_OF_DAY,ehour);
					c.set(Calendar.MINUTE,eminute);
					c.set(Calendar.SECOND,esecond);
					c.set(Calendar.MILLISECOND,emilis);
					Date newEnd = c.getTime();
					
					rdates.add(new DatesRange(newStart, newEnd));
				}
			} else {
				
				// se tiene en cuenta cualquier appointment que surga en el día indicado por la fecha
				
				Calendar c = Calendar.getInstance();
				
				for (Date date : dates) {
					c.setTime(date);
					c.set(Calendar.HOUR_OF_DAY,0);
					c.set(Calendar.MINUTE,0);
					c.set(Calendar.SECOND,0);
					c.set(Calendar.MILLISECOND,0);
					Date newStart = c.getTime();
					
					c.set(Calendar.HOUR_OF_DAY,23);
					c.set(Calendar.MINUTE,59);
					c.set(Calendar.SECOND,59);
					c.set(Calendar.MILLISECOND,999);
					Date newEnd = c.getTime();
					
					rdates.add(new DatesRange(newStart, newEnd));
				}
			}
			
			
			 List<ReserveAttemptDate> rads2 = findAll(app,rdates);
			 if (rads2 != null && rads2.size() > 0) {
				 rads.addAll(rads2);
			 }
			
			return rads;
			
		} catch (PersonException e) {
			throw new MapauException(e);
		}
		
	};
	
	private List<ReserveAttemptDate> findAll(ReserveAttemptDate rad,	List<DatesRange> dates) throws MapauException, PersonException {
		
		if (rad.getCourse() == null) {
			throw new MapauException("rad.course == null");
		}
		
		if (rad.getType() == null) {
			throw new MapauException("rad.type == null");
		}
		
		if (rad.getStudentGroup() == null) {
			throw new MapauException("rad.studentGroup == null");
		}
		
		List<ReserveAttemptDate> rads = new ArrayList<ReserveAttemptDate>();
		for (DatesRange dr : dates) {
			List<ReserveAttemptDate> rads2 = reserveAttemptDateDAO.findBy(dr.getStart(), dr.getEnd());
			for (ReserveAttemptDate rd : rads2) {
				// el curso, tipo y comisión deben ser iguales.
				if (rad.getCourse().equals(rd.getCourse()) && rad.getType().equals(rd.getType()) && rad.getStudentGroup().equals(rd.getStudentGroup())) {
					rads.add(rd);
				}
			}
		}
		return rads;
		
	}
	
	
	private boolean isClassRoomAvailableCapacity(ClassRoom classRoom, Long capacity){		
		List<CharacteristicQuantity> characteristicQuantitys = classRoom.getCharacteristicQuantity();
		
		if (characteristicQuantitys == null) {
			return false;
		}
		
		if(capacity <= getCapacity(characteristicQuantitys)) return true;
		
		return false;
		
	}
	
	private Long getCapacity(List<CharacteristicQuantity> characteristicQuantitys) {
		
		if (characteristicQuantitys == null) {
			return 0l;
		}
		
		for (CharacteristicQuantity characteristicQuantity : characteristicQuantitys) {
			Characteristic characteristic = characteristicQuantity.getCharacteristic();
								
			if (characteristic.getName() == null) {
				continue;
			}
			
			if (characteristic.getName().equals("Capacidad")) {
				if (characteristicQuantity.getQuantity() != null) {
					return characteristicQuantity.getQuantity();
				}
			}
		}
		
		return 0l;
	}
		
	private Long getCapacityReserve(List<ReserveAttemptDate> reserveAttemptDates) {
		Long capacity = 0l;
		
		for (ReserveAttemptDate rad : reserveAttemptDates) {
			Long capacityActual = getCapacity(rad.getCharacteristicsQuantity());
			if (capacity < capacityActual) {
				capacity = capacityActual;
			}
		}
		
		return capacity;
	}
	
	@Override
	public List<ClassRoom> findAllClassRoomsAvailableIn(List<ReserveAttemptDate> rads, boolean checkCapacity) throws MapauException {
		List<ClassRoom> availableClassRooms = findAllClassRoomsAvailableIn(rads);
		List<ClassRoom> classRoomsLessCapacity = new ArrayList<ClassRoom>();
		
		if(checkCapacity) {
			Long capacity = getCapacityReserve(rads);
			for(ClassRoom classRoom : availableClassRooms) {
				if (!isClassRoomAvailableCapacity(classRoom, capacity)) {
					classRoomsLessCapacity.add(classRoom);
				}
			}
		}
		availableClassRooms.removeAll(classRoomsLessCapacity);
		return availableClassRooms;  
		
	}
	
	/**
	 * obtengo los cursos que no se pueden pisar por finales del area correspondiente al rad
	 * @param rad
	 * @return
	 * @throws MapauException 
	 */
	private List<Course> findUntouchableSubjectsBy(ReserveAttemptDate rad) throws MapauException {
		
		Area area = rad.getArea();
		if (area == null) {
			return new ArrayList<Course>();	
		}
		
		UntouchableSubject untouchableSubject = untouchableSubjectsManager.findBy(area);
		if (untouchableSubject == null) {
			return new ArrayList<Course>();
		}
		
		return untouchableSubject.getCourses();
	}
	
	private boolean includeCourses(Course course, List<Course> courses) {
		if (course == null || course.getId() == null) {
			return false;
		}
		
		for (Course c : courses) {
			if (course.getId().equals(c.getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<ClassRoom> findAllClassRoomsAvailableIn(List<ReserveAttemptDate> rads) throws MapauException {

		List<ClassRoom> availableClassRooms = new ArrayList<ClassRoom>();

		if (rads == null || rads.size() <= 0) {
			return availableClassRooms;
		}
		
		List<ClassRoom> allClassrooms = classRoomsManager.findAll();
		if (allClassrooms == null || allClassrooms.size() <= 0) {
			return availableClassRooms; 
		}		
		availableClassRooms.addAll(allClassrooms);

		List<DatesRange> rdates = new ArrayList<DatesRange>();
		for (ReserveAttemptDate r : rads) {
			rdates.add(new DatesRange(r.getStart(), r.getEnd()));
		}

		try {
			List<ClassRoom> usedClassRooms = new ArrayList<ClassRoom>();
			List<ReserveAttemptDate> collidingRads = reserveAttemptDateDAO.findAllCollidingWith(rdates);
			
			
			//verifico si es un final con lo que estoy tratando
			if (rads.get(0).getType().getName().equals("Final")) {
				//obtengo los cursos que no se pueden pisar por finales del area correspondiente
				List<Course> courses = findUntouchableSubjectsBy(rads.get(0));
				
				//elimino todas las reservas que no tenga de tipo final o que no sea una de las materias intocables
				Iterator<ReserveAttemptDate> it = collidingRads.iterator();
				while (it.hasNext()) {
					ReserveAttemptDate rad = it.next();
					if (!rad.getType().getName().equals("Final") && !includeCourses(rad.getCourse(), courses)) {
						it.remove();
					}
				}				
			}
			
			for (ReserveAttemptDate r : collidingRads) {
				List<Reserve> res = reservesManager.findBy(r);
				if (res != null && res.size() > 0) {
					for (Reserve reserve : res) {
						usedClassRooms.add(reserve.getClassRoom());
					}
				}
			}
			availableClassRooms.removeAll(usedClassRooms);
	
			return availableClassRooms;
			
		} catch (PersonException e) {
			throw new MapauException(e);
			
		} catch (UnsupportedOperationException e) {
			throw new MapauException(e);
		}
	}
	
	
	
}
