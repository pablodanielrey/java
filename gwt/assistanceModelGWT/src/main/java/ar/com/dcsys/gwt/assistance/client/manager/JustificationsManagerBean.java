package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public class JustificationsManagerBean implements JustificationsManager {

	
	public JustificationsManagerBean() {
		
	}
	
	@Override
	public void getJustifications(Receiver<List<Justification>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(Justification justification, Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Justification justification, Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void justify(PersonValueProxy person, Date start, Date end,
			Justification justification, String notes, Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findBy(List<Person> persons, Date start, Date end,
			Receiver<List<JustificationDate>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findByPersonValue(List<PersonValueProxy> persons, Date start,
			Date end, Receiver<List<JustificationDate>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(List<JustificationDate> justifications,
			Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(List<GeneralJustificationDate> justifications,
			Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findGeneralJustificationDateBy(Date start, Date end,
			Receiver<List<GeneralJustificationDate>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGeneralJustificationDate(
			List<GeneralJustificationDate> justifications,
			Receiver<Void> receiver) {
		// TODO Auto-generated method stub
		
	}

}
