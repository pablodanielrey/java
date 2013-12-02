package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;

public class ReserveAttemptBean implements Serializable, ReserveAttempt {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	private Long version = 1l;
	private String description;
	private Person owner;
	private List<Person> relatedPersons = new ArrayList<Person>();
	private Date created = new Date();
	
	
	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	public String getId() {
		if (this.id == null) {
			return null;
		}
		return this.id.toString();
	}
	
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public List<Person> getRelatedPersons() {
		return relatedPersons;
	}

	public void setRelatedPersons(List<Person> relatedPersons) {
		this.relatedPersons = relatedPersons;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
}