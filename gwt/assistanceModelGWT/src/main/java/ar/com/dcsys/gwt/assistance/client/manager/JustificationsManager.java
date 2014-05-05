package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public interface JustificationsManager {
	
	public void getJustifications(Receiver<List<Justification>> receiver);
	
	public void persist(Justification justification, Receiver<Void> receiver);
	
	public void remove(Justification justification, Receiver<Void> receiver);
	
	public void justify(Person person, Date start, Date end, Justification justification, String notes, Receiver<Void> receiver);
	
	public void findBy(List<Person> persons, Date start, Date end, Receiver<List<JustificationDate>> receiver);
	
	public void findByPersonValue(List<PersonValueProxy> persons, Date start, Date end, Receiver<List<JustificationDate>> receiver);
	
	public void remove(List<JustificationDate> justifications, Receiver<Void> receiver);
	
	public void persist(List<GeneralJustificationDate> justifications, Receiver<Void> receiver);
	
	public void findGeneralJustificationDateBy(Date start, Date end, Receiver<List<GeneralJustificationDate>> receiver);
	
	public void removeGeneralJustificationDate(List<GeneralJustificationDate> justifications, Receiver<Void> receiver);

}
