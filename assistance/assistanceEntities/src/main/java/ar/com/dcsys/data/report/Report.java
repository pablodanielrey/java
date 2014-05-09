package ar.com.dcsys.data.report;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;

public interface Report {

	public String getGroupName();
	public String getName();
	public Date getDate();
	public String getJustification();
	public String getGeneralJustification();
	public Person getPerson();
	public List<Group> getGroups();
	public Group getGroup();
	public Period getPeriod();
	public List<Justification> getJustifications();
	public List<Justification> getGjustifications();
	public Long getMinutes();
	public Boolean isAbscence();
	
}
