package ar.com.dcsys.gwt.assistance.server;

import java.util.Arrays;
import java.util.List;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.assistance.shared.PeriodsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.model.period.PeriodsManager;

import javax.inject.Inject;

public class PeriodsManagerTransferBean implements PeriodsManagerTransfer {
	
	private final PeriodsManager periodsManager;
	
	@Inject
	public PeriodsManagerTransferBean(PeriodsManager periodsManager) {
		this.periodsManager = periodsManager;
	}

	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findPersonsWithPeriodAssignation(Receiver<List<Person>> receiver) {
		try {
			List<Person> persons = periodsManager.findPersonsWithPeriodAssignations();
			receiver.onSuccess(persons);
		} catch (PeriodException | PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findPeriodsAssignationsBy(Person person,Receiver<List<PeriodAssignation>> receiver) {
		try {
			List<PeriodAssignation> periods = periodsManager.findAll(person);
			receiver.onSuccess(periods);
		} catch (PeriodException e) {
			receiver.onError(e.getMessage());
		}		
	}

	@Override
	public void findAllTypesPeriods(Receiver<List<PeriodType>> receiver) {
		try {
			List<PeriodType> types = Arrays.asList(PeriodType.values());
			receiver.onSuccess(types);
		} catch (Exception e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void remove(Person person, PeriodAssignation periodAssignation,Receiver<Void> receiver) {
		try {
			//verifico que  exista
	        PeriodAssignation pa = periodsManager.findBy(person, periodAssignation.getStart(), periodAssignation.getType());
	        if (pa == null) {
	        	receiver.onSuccess(null);
	            return;
	        }
	        
	        periodAssignation.setPerson(person);
	        periodsManager.remove(periodAssignation);
	        
	        receiver.onSuccess(null);
		} catch (PeriodException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void persist(Person person, PeriodAssignation periodAssignation,	Receiver<Void> receiver) {
		try {
			//verifico que  exista
	        PeriodAssignation pa = periodsManager.findBy(person, periodAssignation.getStart(), periodAssignation.getType());
	        if (pa == null) {
	        	receiver.onSuccess(null);
	            return;
	        }
	        
	        periodAssignation.setPerson(person);
	        periodsManager.persist(periodAssignation);
	        
	        receiver.onSuccess(null);
		} catch (Exception e) {
			receiver.onError(e.getMessage());
		}
	}

}
