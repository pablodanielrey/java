package ar.com.dcsys.model.justification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDAO;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.JustificationDateBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.log.AttLogsManager;
import ar.com.dcsys.model.period.Period;
import ar.com.dcsys.model.period.PeriodsManager;


public class JustificationsManagerBean implements JustificationsManager {

	private static final long serialVersionUID = 1L;

	private JustificationDAO justificationManager;
	
//	@EJB Configuration config;
//	@EJB AuthsManager authsManager;
	private final PersonsManager personsManager;
	private final PeriodsManager periodsManager;
	private final AttLogsManager logManager;
//	@Resource SessionContext ctx;
	
	
	@Inject
	public JustificationsManagerBean(JustificationDAO justificationDAO, PersonsManager personsManager, PeriodsManager periodsManager, AttLogsManager attLogsManager) {
		this.justificationManager = justificationDAO;
		this.personsManager = personsManager;
		this.periodsManager = periodsManager;
		this.logManager = attLogsManager;
	}
	
	
	@Override
	public Justification findById(String id) throws JustificationException {
		return justificationManager.findById(id);
	}

	@Override
	public List<Justification> findAll() throws JustificationException {
		return justificationManager.findAll();
	}

	@Override
	public void persist(Justification j) throws JustificationException {
		justificationManager.persist(j);
	}
	
	@Override
	public void remove(Justification j) throws JustificationException {
		justificationManager.remove(j);
	}

	@Override
	public JustificationDate findJustificationDateById(String id) throws JustificationException, PersonException {
		return justificationManager.findJustificationDateById(id);
	}

	@Override
	public void persist(JustificationDate jd) throws JustificationException, PersonException {
		justificationManager.persist(jd);
	}
	
	@Override
	public void remove(List<JustificationDate> jds)	throws JustificationException, PersonException {
		if (jds == null || jds.size() <= 0) {
			return;
		}
		
		try {
			for (JustificationDate jd : jds) {
				justificationManager.remove(jd);
			}
		} catch (Exception e) {
			throw new PersonException(e);
		}
		
	}

	@Override
	public GeneralJustificationDate findGeneralJustificationDateById(String id)	throws JustificationException {
		return justificationManager.findGeneralJustificationDateById(id);
	}

	@Override
	public void persist(GeneralJustificationDate gjd) throws JustificationException {
		justificationManager.persist(gjd);
	}

	@Override
	public void persist(List<GeneralJustificationDate> gjds) throws JustificationException {
		for (GeneralJustificationDate generalJustificationDate : gjds) {
			persist(generalJustificationDate);
		}
	}
	
	@Override
	public void justify(Person person, Date start, Date end, Justification justification, boolean onlyWorkDays, String notes) throws JustificationException, PersonException {
		
		try {
			// generar todas las justificaciones dentro de esas fechas.
			// por lo que hay que generar los períodos a justificar.
			List<Period> periods = periodsManager.findAll(person, start, end, onlyWorkDays);
			
			justify(person,periods,justification, notes);
			
		} catch (PeriodException e) {
			throw new JustificationException(e);
		}
		
	}
	
	@Override
	public void justify(String personId, Date start, Date end, Justification justification, boolean onlyWorkDays, String notes) throws JustificationException, PersonException {
		Person person = personsManager.findById(personId);
		this.justify(person, start, end, justification, onlyWorkDays, notes);
	}
	
	@Override
	public List<JustificationDate> findBy(List<Person> persons, Date start,	Date end) throws JustificationException, PersonException {
		List<JustificationDate> dates = new ArrayList<>();
		for (Person p : persons) {
			dates.addAll(justificationManager.findBy(p, start, end));
		}
		return dates;
	}

	@Override
	public void justify(Person person, List<Period> periods, Justification justification, String notes) throws JustificationException, PersonException {
		for (Period p : periods) {
			Date s = p.getStart();
			Date e = p.getEnd();
			
			JustificationDate jd = new JustificationDateBean();
			jd.setStart(s);
			jd.setEnd(e);
			jd.setJustification(justification);
			jd.setPerson(person);
			jd.setNotes(notes);
			persist(jd);
		}	
	}
	
	@Override
	public void justify(String personId, List<Period> periods, Justification justification, String notes) throws JustificationException, PersonException {
		Person person = personsManager.findById(personId);
		this.justify(person, periods, justification, notes);
	}

	@Override
	public List<GeneralJustificationDate> findGeneralJustificationDateBy (Date start, Date end) throws JustificationException {
		return justificationManager.findGeneralJustificationDateBy (start,end);
	}
	
	@Override
	public void removeGeneralJustificationDate (GeneralJustificationDate gjd) throws JustificationException {
		justificationManager.removeGeneralJustificationDate(gjd);
	}

	@Override
	public void removeGeneralJustificationDate (List<GeneralJustificationDate> gjd) throws JustificationException {
		for (GeneralJustificationDate generalJustificationDate : gjd) {
			removeGeneralJustificationDate(generalJustificationDate);
		}
	}

}
