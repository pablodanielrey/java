package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.assistance.shared.PeriodsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

public class PeriodsManagerBean implements PeriodsManager {

	private static Logger logger = Logger.getLogger(PeriodsManagerBean.class.getName());
	
	private final WebSocket socket;
	
	private final PeriodsManagerTransfer periodsManagerTransfer = GWT.create(PeriodsManagerTransfer.class);
	
	@Inject
	public PeriodsManagerBean(WebSocket ws) {
		this.socket = ws;
		this.periodsManagerTransfer.setTransport(ws);
	}
	
	@Override
	public void findAllPeriods(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec) {
		periodsManagerTransfer.findAllPeriods(start, end, persons, rec);
	}
	
	@Override
	public void findAllPeriods(Date start, Date end, List<Person> persons, Boolean onlyWorkDays, Receiver<ReportSummary> rec) {
		periodsManagerTransfer.findAllPeriods(start, end, persons, onlyWorkDays, rec);
	}
	
	@Override
	public void findAllAbsences(Date start, Date end, List<Person> persons,	Receiver<ReportSummary> rec) {
		periodsManagerTransfer.findAllAbsences(start, end, persons, rec);
	}
	
	@Override
	public void findPersonsWithPeriodAssignation(Group group, Receiver<List<Person>> receiver) {
		try {
			if (group != null) {
				this.periodsManagerTransfer.findPersonsWithPeriodAssignation(group,receiver);
			} else {
				this.periodsManagerTransfer.findPersonsWithPeriodAssignation(receiver);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void findPeriodsAssignationsBy(Person person, Receiver<List<PeriodAssignation>> receiver) {
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
