package ar.com.dcsys.gwt.assistance.shared;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface PeriodsManagerTransfer extends Manager {
	
	public void findPersonsWithPeriodAssignation(Group group, Receiver<List<Person>> receiver);
	public void findPersonsWithPeriodAssignation(Receiver<List<Person>> receiver);
	
	public void findPeriodsAssignationsBy(Person person, Receiver<List<PeriodAssignation>> receiver);
	public void findAllTypesPeriods(Receiver<List<PeriodType>> receiver);
	
	public void remove(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver); 
	public void persist(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver);

}
