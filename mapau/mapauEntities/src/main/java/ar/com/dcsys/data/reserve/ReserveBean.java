package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;

public class ReserveBean implements Serializable, Reserve {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	private Long version = 1l;
	
	private ClassRoom classRoom;
	private String description;
	private Person owner;
	private Date created;
	private Reserve related;
	private List<Person> relatedPersons;
	private ReserveAttemptDate reserveAttemptDate;

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Reserve getRelated() {
		return related;
	}

	public void setRelated(Reserve related) {
		this.related = related;
	}

	public List<Person> getRelatedPersons() {
		return relatedPersons;
	}

	public void setRelatedPersons(List<Person> relatedPersons) {
		this.relatedPersons = relatedPersons;
	}

	public ReserveAttemptDate getReserveAttemptDate() {
		return reserveAttemptDate;
	}

	public void setReserveAttemptDate(ReserveAttemptDate reserveAttempt) {
		this.reserveAttemptDate = reserveAttempt;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
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

}