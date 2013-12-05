package ar.com.dcsys.model.reserve;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveBean;
import ar.com.dcsys.data.reserve.ReserveDAO;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

@Singleton
public class ReservesManagerBean implements ReservesManager {
	
	private static Logger logger = Logger.getLogger(ReservesManagerBean.class.getName());
	
	private final ReserveDAO reserveDAO;
	
	
	@Inject
	public ReservesManagerBean(ReserveDAO reserveDAO) {
		this.reserveDAO = reserveDAO;
		createCaches();
	}
	
	private void createCaches() {
		
	}
	
	private List<Reserve> findReserveRelatedWithId(String id) throws MapauException {
		return reserveDAO.findReserveRelatedWithId(id);
	}
	
	@Override
	public List<Reserve> findAllByDates(Date start, Date end) throws MapauException {
		try {
			return reserveDAO.findAllByDates(start, end);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public List<Reserve> findAllCollidingWith(Date start, Date end, List<ClassRoom> classRooms) throws MapauException {
		return reserveDAO.findAllCollidingWith(start, end, classRooms);
	}
	
	/**
	 * Retorna las reservas ULTIMAS para un date específico.
	 * no trae las reservas que representan el historial de modificaciones de reservas de esa fecha.
	 */
	@Override
	public List<Reserve> findBy(ReserveAttemptDate date) throws MapauException {
		try {
			List<Reserve> reserves = reserveDAO.findBy(date);
			Iterator<Reserve> it = reserves.iterator();
			while (it.hasNext()) {
				if (it.next().getRelated() != null) {
					it.remove();
				}
			}
			return reserves;
			
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public Reserve findById(String id) throws MapauException {
		try {
			return reserveDAO.findById(id);
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}
	
	@Override
	public String persist(Reserve reserve) throws MapauException {
		return reserveDAO.persist(reserve);
	}
	
	@Override
	public void remove(Reserve reserve) throws MapauException {
		reserveDAO.remove(reserve);
	}
	
	
	/**
	 * Crea una nueva reserva para la fecha dada por parámetro.
	 * Se indica el aula, y la descripción.
	 * Las personas relacionadas se setean a null, por lo que se mostrará las que tiene el reserveAttempt orgiinal. (se pueden modificar
	 * posteriormente modificando la reserva)
	 */
	@Override
	public Reserve createNew(ReserveAttemptDate date, ClassRoom classRoom, String description) throws MapauException {
		
		if (date == null || date.getId() == null) {
			throw new MapauException("reserveattemptdate == null");
		}

		if (classRoom == null || classRoom.getId() == null) {
			throw new MapauException("classRoom == null");
		}
		
		//try {
		
			List<Reserve> reserves = findBy(date);
			if (reserves != null && reserves.size() > 0) {
				
				String cid = classRoom.getId();
				for (Reserve related : reserves) {
					if (related.getClassRoom().getId().equals(cid)) {
						throw new MapauException("Aula ya reservada para esa fecha y horario");
					}
				}
			}
			
			/*
			 * TODO: falta ver lo del principal en el nuevo modelo
			 */
		/*	DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person owner = personsManager.findByPrincipal(principal);*/
			
			Reserve reserve = new ReserveBean();
			reserve.setClassRoom(classRoom);
			reserve.setReserveAttemptDate(date);
			reserve.setCreated(new Date());
		//	reserve.setOwner(owner);
			reserve.setDescription(description);
			
			persist(reserve);
			
			return reserve;
			
	/*	} catch (PersonException e) {
			throw  new MapauException(e);
		}*/
	}
	
	/**
	 * TODO:!!!! chequearrrr porque comente para que funque con la borrada del manager 
	 * @param dates
	 * @return
	 * @throws MapauException
	 */
	public List<ClassRoom> getClassRoomsEmtyIn(List<ReserveAttemptDate> dates) throws MapauException {
		

			// obtengo los grupos de la persona.
			
			/*
			 * TODO: comente para que compile 3/12
			 */
		/*	DCSysPrincipal principal = authsManager.getUserPrincipal();
			Person person = personsManager.findByPrincipal(principal);
			List<Group> groups = groupsManager.findByPerson(person);
			*/
			// FIN del comentario 3/12
			
			// obtengo las aulas que me están permitido administrar.
			//List<ClassRoomGroup> cgroups = classRoomGroupsManager.findBy(groups);
			List<ClassRoom> classRooms = new ArrayList<>();
			
			/*
			for (ClassRoomGroup crg : cgroups) {
				for (ClassRoom cr : crg.getClassrooms()) {
					boolean found = false;
					for (ClassRoom craux : classRooms) {
						if (cr.getId().equals(craux.getId())) {
							found = true;
							break;
						}
					}
					if (!found) {
						classRooms.add(cr);
					}
				}
			}*/
		
			// obtengo las aulas que tienen reservas en los horarios de las fechas pasadas por parámetro.
			List<ClassRoom> notEmpty = new ArrayList<>();
			for (ReserveAttemptDate date : dates) {
				List<Reserve> colliding = findAllCollidingWith(date.getStart(),date.getEnd(),classRooms);
				for (Reserve r : colliding) {
					notEmpty.add(r.getClassRoom());
				}
			}
			
			// remuevo las que tienen reservas.
			Iterator<ClassRoom> it = classRooms.iterator();
			while (it.hasNext()) {
				ClassRoom c = it.next();
				for (ClassRoom c2 : notEmpty) {
					if (c.getId().equals(c2.getId())) {
						it.remove();
					}
				}
			}
			
			return classRooms;

	}

	
	@Override
	public List<Reserve> findAllCollidingWith(List<DatesRange> dates) throws MapauException, PersonException {
		return reserveDAO.findAllCollidingWith(dates);
	}
	
	
}
