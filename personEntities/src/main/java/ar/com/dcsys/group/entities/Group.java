package ar.com.dcsys.group.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.group.entities.types.GroupType;
import ar.com.dcsys.person.entities.Mail;
import ar.com.dcsys.person.entities.Person;


public class Group implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private String name;
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
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	public Long getVersion() {
		return 1L;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
