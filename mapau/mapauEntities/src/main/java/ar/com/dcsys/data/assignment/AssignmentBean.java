package ar.com.dcsys.data.assignment;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;


public class AssignmentBean implements Serializable, Assignment {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	private Long version = 1l;
	private Date from;
	private Date to;
	private String notes;
	private Person person;
	private Course course;
	private Assignment relatedAssignment;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(String id) {
		if (id == null) {
			this.id = null;
			return ;
		}
		
		this.id = UUID.fromString(id);
	}
	
	public String getId(){
		if (this.id == null) {
			return null;
		}

		return this.id.toString();
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Course getCourse() {
		return course;
	}

	@Override
	public void setCourse(Course course) {
		this.course = course;
	}

	public Assignment getRelatedAssignment() {
		return relatedAssignment;
	}

	public void setRelatedAssignment(Assignment relatedAssignment) {
		this.relatedAssignment = relatedAssignment;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
	
}
