package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PersonException;

public interface JustificationDAO extends Serializable {

	/// justification
	
	public Justification findById(String id) throws JustificationException;
	public List<Justification> findAll() throws JustificationException;
	public String persist(Justification j) throws JustificationException;
	public void remove(Justification j) throws JustificationException;

	// justification date
	
	public JustificationDate findJustificationDateById(String id) throws JustificationException, PersonException;
	public List<JustificationDate> findBy(Person person, Date start, Date end) throws JustificationException, PersonException;
	public String persist(JustificationDate jd) throws JustificationException;
	public void remove(JustificationDate jd) throws JustificationException;
	
	// general justifications
	
	public GeneralJustificationDate findGeneralJustificationDateById(String id) throws JustificationException;
	public List<GeneralJustificationDate> findGeneralJustificationDateBy(Date start, Date end) throws JustificationException;
	public String persist(GeneralJustificationDate gjd) throws JustificationException;
	public void removeGeneralJustificationDate (GeneralJustificationDate gjd) throws JustificationException;

	
	public interface Params {
		public Person findPersonById(String id) throws PersonException;
		public Justification findJustificationById(String id) throws JustificationException;
	}

}
