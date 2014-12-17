package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface PeriodsManager {

	public void findPersonsWithPeriodAssignation(Group group, Receiver<List<Person>> receiver);
	public void findPeriodsAssignationsBy(Person person, Receiver<List<PeriodAssignation>> receiver);
	
	public void remove(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver); 
	public void persist(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver);

	
	public void findAllPeriods(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec);
	public void findAllPeriods(Date start, Date end, List<Person> persons, Boolean onlyWorkDays,Receiver<ReportSummary> rec);
	public void findAllAbsences(Date start, Date end, List<Person> persons,Receiver<ReportSummary> rec);
	
}
