package ar.com.dcsys.model.reports.assistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.model.period.Period;
import ar.com.dcsys.model.period.WorkedHours;

public class Report {
	
	private Person person;
	private Group group;
	private List<Group> groups;
	private Period period;
	private List<Justification> justifications = new ArrayList<>();
	private List<Justification> gjustifications = new ArrayList<>();
	
	private Long minutes = 0l;
	private Boolean isAbscence;
		
	
	/// para el reporte /////////
	
	public String getGroupName() {
		if (group == null || group.getName() == null) {
			return "nulo";
		}
		return group.getName();
	}
	
	public String getName() {
		Person person = getPerson();
		if (person == null) {
			return "nulo";
		}
		
		StringBuilder sb = new StringBuilder();
		if (person.getName() != null) {
			sb.append(person.getName());
		} else {
			sb.append("nulo");
		}
		
		if (person.getLastName() != null) {
			sb.append(" " + person.getLastName());
		} else {
			sb.append(" nulo");
		}
		
		return sb.toString();
	}
	
	public Date getDate() {
		return getPeriod().getStart();
	}
	
	public String getJustification() {
		if (getJustifications() == null || getJustifications().size() <= 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (Justification j : getJustifications()) {
			sb.append(j.getDescription());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public String getGeneralJustification() {
		if (getGjustifications() == null || getGjustifications().size() <= 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (Justification j : getGjustifications()) {
			sb.append(j.getDescription());
			sb.append(" ");
		}
		return sb.toString();
	}	
	
	///////////////
	
	
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Period getPeriod() {
		return period;
	}
	
	public void setPeriod(Period period) {
		this.period = period;
	}

	public List<Justification> getJustifications() {
		return justifications;
	}

	public void setJustifications(List<Justification> justifications) {
		this.justifications = justifications;
	}

	public List<Justification> getGjustifications() {
		return gjustifications;
	}

	public void setGjustifications(List<Justification> gjustifications) {
		this.gjustifications = gjustifications;
	}

	
	public void calcReportData() {
		List<WorkedHours> whs = period.getWorkedHours();
		
		if (whs == null || whs.size() == 0) {
			isAbscence = true;
			minutes = 0l;
		} else {
			isAbscence = false;
			
			// calculo los minutos totales trabajados.
			minutes = 0l;
			for (WorkedHours wh : whs) {
				minutes = minutes + (wh.getWorkedMilis() / 1000l);
			}
		}
		
	}
	
	public Long getMinutes() {
		return minutes;
	}

	public Boolean isAbscence() {
		return isAbscence;
	}
	
	
}
