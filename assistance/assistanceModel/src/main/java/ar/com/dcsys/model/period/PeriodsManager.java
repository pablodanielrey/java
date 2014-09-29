package ar.com.dcsys.model.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;


public interface PeriodsManager {
	
	public List<Period> findAll(Person person, Date start, Date end) throws PeriodException;
	public List<Period> findAll(Person person, Date start, Date end, boolean onlyWorkDays) throws PeriodException;
	
	public List<PeriodAssignation> findAll(Person p) throws PeriodException;
	
	public PeriodAssignation findBy(Person person, Date date, String type) throws PeriodException;
	public List<PeriodAssignation> findBy(Person person, Date date, PeriodType type) throws PeriodException;
	
	public List<Person> findPersonsWithPeriodAssignations() throws PeriodException, PersonException;
	public List<Person> findActivePersons() throws PeriodException, PersonException;
	
	public void persist(PeriodAssignation p) throws PeriodException;
	public void remove(PeriodAssignation p) throws PeriodException;
}
