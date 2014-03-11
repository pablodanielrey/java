package ar.com.dcsys.data.period;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;

public interface PeriodDAO extends Serializable {
	
	public List<PeriodAssignation> findAllPeriodAssignations() throws PeriodException, PersonException;
	public List<PeriodAssignation> findAll(Person p) throws PeriodException;
	
	public void persist(PeriodAssignation p) throws PeriodException;
	public void remove(PeriodAssignation p) throws PeriodException;
	
	public PeriodAssignation findBy(Person person, Date date, PeriodType type) throws PeriodException;
	
	public interface Params {
		public Person findPersonById(String person_id) throws PersonException;
	}
}
