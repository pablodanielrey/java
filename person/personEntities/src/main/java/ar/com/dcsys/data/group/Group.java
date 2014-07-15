package ar.com.dcsys.data.group;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;


public class Group {

	private String id;
	private String name;
	private List<String> systems = new ArrayList<String>();
	private List<Mail> mails = new ArrayList<Mail>();
	private List<Person> persons = new ArrayList<Person>();
	private List<GroupType> types = new ArrayList<GroupType>();

	public List<Mail> getMails() {
		return mails;
	}

	public void setMails(List<Mail> mails) {
		this.mails = mails;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<GroupType> getTypes() {
		return types;
	}

	public void setTypes(List<GroupType> types) {
		this.types = types;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
		
	public List<String> getSystems() {
		return systems;
	}

	public void setSystems(List<String> systems) {
		this.systems = systems;
	}

	
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}
