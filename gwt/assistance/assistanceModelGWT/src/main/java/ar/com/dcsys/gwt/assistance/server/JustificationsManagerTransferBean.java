package ar.com.dcsys.gwt.assistance.server;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.assistance.shared.JustificationsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.model.justification.JustificationsManager;

import javax.inject.Inject;

public class JustificationsManagerTransferBean implements JustificationsManagerTransfer {
	
	private final JustificationsManager justificationsManager;
	
	@Inject
	public JustificationsManagerTransferBean(JustificationsManager justificationsManager) {
		this.justificationsManager = justificationsManager;
	}

	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getJustifications(Receiver<List<Justification>> receiver) {
		try {
			List<Justification> justifications = justificationsManager.findAll();
			receiver.onSuccess(justifications);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void persist(Justification justification, Receiver<Void> receiver) {
		try {
			justificationsManager.persist(justification);
			receiver.onSuccess(null);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void remove(Justification justification, Receiver<Void> receiver) {
		try {
			justificationsManager.remove(justification);
			receiver.onSuccess(null);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void justify(Person person, Date start, Date end,Justification justification, String notes, Receiver<Void> receiver) {
		try {
			justificationsManager.justify(person,start,end,justification,false,notes);
			receiver.onSuccess(null);
		} catch (JustificationException | PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findBy(List<Person> persons, Date start, Date end,Receiver<List<JustificationDate>> receiver) {
		try {
			List<JustificationDate> jds = justificationsManager.findBy(persons, start, end);
			receiver.onSuccess(jds);
		} catch (JustificationException | PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findByPersonValue(List<PersonValueProxy> persons, Date start,Date end, Receiver<List<JustificationDate>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(List<JustificationDate> justifications,Receiver<Void> receiver) {
		try {
			justificationsManager.remove(justifications);
			receiver.onSuccess(null);
		} catch (JustificationException | PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void persist(List<GeneralJustificationDate> justifications,Receiver<Void> receiver) {
		try {
			justificationsManager.persist(justifications);
			receiver.onSuccess(null);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}		
	}

	@Override
	public void findGeneralJustificationDateBy(Date start, Date end,Receiver<List<GeneralJustificationDate>> receiver) {
		try {
			List<GeneralJustificationDate> gjds = justificationsManager.findGeneralJustificationDateBy(start, end);
			receiver.onSuccess(gjds);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void removeGeneralJustificationDate(List<GeneralJustificationDate> justifications,Receiver<Void> receiver) {
		try {
			justificationsManager.removeGeneralJustificationDate(justifications);
			receiver.onSuccess(null);
		} catch (JustificationException e) {
			receiver.onError(e.getMessage());
		}		
	}

}
