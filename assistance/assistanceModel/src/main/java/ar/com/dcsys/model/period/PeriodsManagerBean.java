package ar.com.dcsys.model.period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodDAO;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.log.AttLogsManager;


public class PeriodsManagerBean implements PeriodsManager {

//	@EJB Configuration config;
	private final AttLogsManager attLogsManager;
	private final PeriodDAO periodManager;
	
	private final PersonsManager personsManager;
	
	private final static PeriodProvider[] providers = {
		new NullPeriodProvider(),
		new DailyPeriodProvider(),
		new SystemPeriodProvider(),
		new WatchmanPeriodProvider()
	};

	@Inject
	public PeriodsManagerBean(PeriodDAO periodDAO, AttLogsManager attLogsManager,PersonsManager personsManager) {
		this.periodManager = periodDAO;
		this.attLogsManager = attLogsManager;
		this.personsManager = personsManager;
	}
	
	
	@Override
	public List<Period> findAll(Person person, Date start, Date end) throws PeriodException {
		return findAll(person, start, end, false);
	}
	
	@Override
	public List<Period> findAll(Person person, Date start, Date end, boolean onlyWorkDays) throws PeriodException {
		
		try {
			
			List<Period> periodsR = new ArrayList<Period>();
			
			// encuentro los tipos de período para ese usuario.
			List<PeriodAssignation> assignations = periodManager.findAll(person);
			
			if (assignations == null || assignations.size() <= 0) {
				return periodsR;
			}
			
			// para que el algoritmo funcione debe estar ordenado de menor a mayor
			Collections.sort(assignations, new Comparator<PeriodAssignation>() {
				@Override
				public int compare(PeriodAssignation arg0, PeriodAssignation arg1) {
					if (arg0 == null && arg1 == null) {
						return 0;
					}
					if (arg0 == null) {
						return -1;
					}
					if (arg1 == null) {
						return 1;
					}
					return arg0.getStart().compareTo(arg1.getStart());
				}
			});
	
			// obtengo el primer período a procesar.
			int initial = 0;
			for (int i = 0; i < assignations.size(); i++) {
				if (!(assignations.get(i).getStart().before(start))) {
					break;
				}
				initial = i;
			}
			
			List<PeriodAssignation> usableAssignations = assignations.subList(initial, assignations.size());
			
			for (int i = 0; i < usableAssignations.size(); i++) {
				PeriodAssignation pa = usableAssignations.get(i);
				Date pstart = pa.getStart();
				Date pend = (((i + 1) < usableAssignations.size()) ? usableAssignations.get(i + 1).getStart() : end);
				PeriodType type = pa.getType();
				
				PeriodProvider pp = findFirstProvider(type);
				if (pp == null) {
					throw new PeriodException("No se puede encontrar el períod provider " + type.name() + " para la persona : " + person.getId());
				}
				List<Period> periods = pp.findPeriods(pstart, pend, start, end, person, attLogsManager, onlyWorkDays);
				if (periods != null && periods.size() > 0) {
					periodsR.addAll(periods);
				}
			}
			
			return periodsR;
		
		} catch (PeriodException e) {
			throw e;
			
		} catch (Exception e) {
			throw new PeriodException(e);
		}
	}

	private PeriodProvider findFirstProvider(PeriodType type) {
		for (PeriodProvider pp : providers) {
			if (pp.isUsable(type)) {
				return pp;
			}
		}
		return null;
	}

	@Override
	public List<PeriodAssignation> findAll(Person p) throws PeriodException {
		return periodManager.findAll(p);
	}
	
	/**
	 * Retorna las personas que tienen algun tipo de período. tambien trae las peronsas que tienen período nulo actualmente.
	 */
	@Override
	public List<Person> findPersonsWithPeriodAssignations() throws PeriodException, PersonException {
		List<PeriodAssignation> periodAssignations = periodManager.findAllPeriodAssignations();
		List<Person> persons = new ArrayList<Person>();
		List<String> ids = new ArrayList<String>();
		for (PeriodAssignation pa : periodAssignations) {
			Person p = pa.getPerson();
			if (p == null) {
				// por ahora ignoro pero despues posiblemente tire la exception.
				continue;
				//throw new PeriodException("Asignación de período con persona nula");
			}
			// uso ids asi funciona el contains de las collections y debe ser implementado mas eficientemente que un for + if.
			if (!ids.contains(p.getId())) {
				persons.add(p);
				ids.add(p.getId());
			}
		}
		return persons;
	}

	/**
	 * Retorna las peronsas que tienen perídos que no son nulos actualmente (como último período)
	 * Representarían las personas activas de la organización.
	 */
	@Override
	public List<Person> findActivePersons() throws PeriodException,	PersonException {
		List<PeriodAssignation> periodAssignations = periodManager.findAllPeriodAssignations();
		
		// ordeno los períodos por fecha. deben estar ordenados para que el algoritmo siguiente funcione.
		Collections.sort(periodAssignations, new Comparator<PeriodAssignation>() {
			@Override
			public int compare(PeriodAssignation arg0, PeriodAssignation arg1) {
				if (arg0 == null && arg1 == null) {
					return 0;
				}
				if (arg0 == null) {
					return -1;
				}
				if (arg1 == null) { 
					return 1;
				}
				return arg0.getStart().compareTo(arg1.getStart());
			}
		});
		
		List<Person> persons = new ArrayList<Person>();
		for (PeriodAssignation pa : periodAssignations) {

			Person p = pa.getPerson();
			if (p == null) {
				// por ahora ignoro pero despues posiblemente tire la exception.
				continue;
				//throw new PeriodException("Asignación de período con persona nula");
			}
			
			if (pa.getType().equals(PeriodType.NULL)) {
				if (persons.contains(p)) {
					persons.remove(p);
				}
			} else {
				if (!persons.contains(p)) {
					persons.add(p);
				}
			}
		}
		return persons;
	}
	
	@Override
	public void persist(PeriodAssignation p) throws PeriodException {
		periodManager.persist(p);
	}

	@Override
	public PeriodAssignation findBy(Person person, Date date, PeriodType type)	throws PeriodException {
		return periodManager.findBy(person, date, type);
	}

	@Override
	public void remove(PeriodAssignation p) throws PeriodException {
		periodManager.remove(p);
	}
	
}
