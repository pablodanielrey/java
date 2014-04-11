package ar.com.dcsys.model.reports;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.data.person.Person;

public class Report {

	private Person person;
	private List<Group> groups;
	

	private String getGroups(GroupType gt) {
		if (groups == null) {
			return "No tiene";
		}
		
		StringBuilder sb = new StringBuilder();
		for (Group g : groups) {
			if (g.getTypes() != null && g.getTypes().contains(gt)) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(g.getName());
			}
		}
		if (sb.length() <= 0) {
			return "No tiene";
		} else {
			return sb.toString();
		}
	}
	
	
	public String getOffices() {
		return getGroups(GroupType.OFFICE);
	}
	
	public String getPositions() {
		return getGroups(GroupType.POSITION);
	}
	
	public String getName() {
		return person.getName();
	}

	public String getLastName() {
		return person.getLastName();
	}

	public String getDni() {
		return person.getDni();
	}

	public String getAddress() {
		return person.getAddress();
	}

	public String getCity() {
		return person.getCity();
	}

	public String getCountry() {
		return person.getCountry();
	}

	public String getGender() {
		if (Gender.M.equals(person.getGender())) {
			return "Masculino";
		}
		if (Gender.F.equals(person.getGender())) {
			return "Femenino";
		}
		return "Desconocido";
	}

	public Date getBirthDate() {
		return person.getBirthDate();
	}

	
	

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

	
	
}
