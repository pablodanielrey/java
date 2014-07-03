package ar.com.dcsys.model.justification;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PersonException;

public interface JustificationsManager extends Serializable {

	/// justification
	
	public Justification findById(String id) throws JustificationException;
	public List<Justification> findAll() throws JustificationException;
	public void persist(Justification j) throws JustificationException;
	public void remove(Justification j) throws JustificationException;

	// justification date
	
	public JustificationDate findJustificationDateById(String id) throws JustificationException, PersonException;
	public List<JustificationDate> findBy(List<Person> persons, Date start, Date end) throws JustificationException, PersonException;
	public void persist(JustificationDate jd) throws JustificationException, PersonException;
	public void remove(List<JustificationDate> jds) throws JustificationException, PersonException;
	
	public void justify(Person person, Date start, Date end, Justification justification, boolean onlyWorkDays, String notes) throws JustificationException, PersonException;
	public void justify(String personId, Date start, Date end, Justification justification, boolean onlyWorkDays, String notes) throws JustificationException, PersonException;
	public void justify(Person person, List<Period> periods, Justification justification, String notes) throws JustificationException, PersonException;
	public void justify(String personId, List<Period> periods, Justification justification, String notes) throws JustificationException, PersonException;
	
	// general justifications
	
	public GeneralJustificationDate findGeneralJustificationDateById(String id) throws JustificationException;
	public List<GeneralJustificationDate> findGeneralJustificationDateBy (	Date start, Date end) throws JustificationException;
	public void persist(GeneralJustificationDate gjd) throws JustificationException;
	public void persist(List<GeneralJustificationDate> gjds) throws JustificationException;
	public void removeGeneralJustificationDate (List<GeneralJustificationDate> gjd) throws JustificationException;
	public void removeGeneralJustificationDate (GeneralJustificationDate gjd) throws JustificationException;
	
	
}
