package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.gin.PeriodsManagerProvider;
import ar.com.dcsys.gwt.assistance.shared.PeriodsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import ar.com.dcsys.gwt.ws.client.WebSocket;

public class PeriodsManagerBean implements PeriodsManager {

	private static Logger logger = Logger.getLogger(PeriodsManagerBean.class.getName());
	
	private final WebSocket socket;
	private final EventBus eventBus;
	
	private final PeriodsManagerTransfer periodsManagerTransfer;
	
	@Inject
	public PeriodsManagerBean(EventBus eventBus,WebSocket ws, PeriodsManagerProvider periodsProvider) {
		this.socket = ws;
		this.eventBus = eventBus;
		this.periodsManagerTransfer = periodsProvider.get();
	}
	
	@Override
	public void findPersonsWithPeriodAssignation(Receiver<List<Person>> receiver) {
		try {
			this.periodsManagerTransfer.findPersonsWithPeriodAssignation(receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void findPeriodsAssignationsBy(Person person,Receiver<List<PeriodAssignation>> receiver) {
		try {
			this.periodsManagerTransfer.findPeriodsAssignationsBy(person, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}		
	}

	@Override
	public void findAllTypesPeriods(Receiver<List<PeriodType>> receiver) {
		try {
			this.periodsManagerTransfer.findAllTypesPeriods(receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void remove(Person person, PeriodAssignation periodAssignation,Receiver<Void> receiver) {
		try {
			this.periodsManagerTransfer.remove(person, periodAssignation, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void persist(Person person, PeriodAssignation periodAssignation,	Receiver<Void> receiver) {
		try {
			this.periodsManagerTransfer.persist(person, periodAssignation, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

}
