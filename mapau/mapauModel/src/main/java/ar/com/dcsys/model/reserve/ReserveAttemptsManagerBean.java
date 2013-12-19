package ar.com.dcsys.model.reserve;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDAO;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDeleted;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;


@Singleton
public class ReserveAttemptsManagerBean implements ReserveAttemptsManager {

	private static Logger logger = Logger.getLogger(ReserveAttemptsManagerBean.class.getName());
	
	private final ReserveAttemptDAO reserveAttemptDAO;

	
	@Inject
	public ReserveAttemptsManagerBean(ReserveAttemptDAO reserveAttemptDAO) {

		this.reserveAttemptDAO = reserveAttemptDAO;  
					
		createCaches();
			
	}
	
	private void createCaches() {
		
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
	
	
	
}
