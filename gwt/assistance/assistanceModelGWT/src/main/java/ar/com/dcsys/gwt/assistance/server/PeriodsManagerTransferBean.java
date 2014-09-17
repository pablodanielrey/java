package ar.com.dcsys.gwt.assistance.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.assistance.shared.PeriodsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.period.PeriodsManager;
import ar.com.dcsys.model.reports.assistance.ReportsModel;

public class PeriodsManagerTransferBean implements PeriodsManagerTransfer {
	
	private final PeriodsManager periodsManager;
	private final GroupsManager groupsManager;
	private final ReportsModel reportsModel;
	
	@Inject
	public PeriodsManagerTransferBean(PeriodsManager periodsManager, GroupsManager groupsManager, ReportsModel reportsModel) {
		this.periodsManager = periodsManager;
		this.groupsManager = groupsManager;
		this.reportsModel = reportsModel;
	}

	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean includePerson(Person person, Group group) throws PersonException {
		List<Group> groups = groupsManager.findByPerson(person);
		if (groups == null || groups.size() <= 0) {
			return false;
		}
		for (Group g : groups) {
			if (g.getId().equals(group.getId())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void findAllPeriods(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec) {
		try {
			ReportSummary summary = reportsModel.reportPeriods(start,end,persons);
			rec.onSuccess(summary);
		} catch (Exception e) {
			rec.onError(e.getMessage());
		}
	}
	
	@Override
	public void findAllPeriods(Date start, Date end, List<Person> persons, Boolean onlyWorkDays, Receiver<ReportSummary> rec) {
		try {
			ReportSummary summary = reportsModel.reportPeriods(start, end, persons, onlyWorkDays);
			rec.onSuccess(summary);
		} catch (Exception e) {
			rec.onError(e.getMessage());
		}
		
	}
	
	@Override
	public void findAllAbsences(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec) {
		try {
			ReportSummary summary = reportsModel.reportPeriods(start, end, persons, true);
			List<Report> reports = summary.getReports();
			List<Report> absences = new ArrayList<Report>();
			
			if (reports != null && reports.size() > 0) {
				for (Report r : reports) {
					Period p = r.getPeriod();
					if (p == null || p.getWorkedHours() == null || p.getWorkedHours().size() <= 0) {
						absences.add(r);
					}
				}
			}
			summary.setReports(absences);
			rec.onSuccess(summary);
 		} catch (Exception e) {
			rec.onError(e.getMessage());
		}
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
	public void findPersonsWithPeriodAssignation(final Group group, final Receiver<List<Person>> receiver) {

		findPersonsWithPeriodAssignation(new Receiver<List<Person>>() {
			@Override
			public void onError(String error) {
				receiver.onError(error);
			}
			
			@Override
			public void onSuccess(List<Person> persons) {
				try {
					if (group == null) {
						receiver.onSuccess(persons);
						return;
					}
					
					List<Person> personsRemove = new ArrayList<Person>();
					
					for (Person p : persons) {
						if (!includePerson(p, group)) {
							personsRemove.add(p);
						}
					}
					
					persons.removeAll(personsRemove);
					receiver.onSuccess(persons);
				
				} catch (PersonException e) {
					receiver.onError(e.getMessage());
				}
				
			}
		});

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
	        if (pa != null) {
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
