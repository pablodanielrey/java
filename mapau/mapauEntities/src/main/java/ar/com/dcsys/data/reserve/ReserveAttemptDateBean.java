package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;

public class ReserveAttemptDateBean implements Serializable, ReserveAttemptDate {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private Date start;
	private Date end;
	private Long version = 1l;
	
	private ReserveAttemptDate related;

	private Course course;
	private List<Person> relatedPersons = new ArrayList<Person>();
	private ReserveAttemptDateType type;
	private List<CharacteristicQuantity> characteristicsQuantity = new ArrayList<CharacteristicQuantity>();
	private String studentGroup;
	private Area area;
	
	private Person creator;
	private Date creationDate;
	private String description;
	
	
	public ReserveAttemptDate getRelated() {
		return related;
	}

	public void setRelated(ReserveAttemptDate related) {
		this.related = related;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Person> getRelatedPersons() {
		return relatedPersons;
	}

	public void setRelatedPersons(List<Person> relatedPersons) {
		this.relatedPersons = relatedPersons;
	}

	public ReserveAttemptDateType getType() {
		return type;
	}

	public void setType(ReserveAttemptDateType type) {
		this.type = type;
	}

	public List<CharacteristicQuantity> getCharacteristicsQuantity() {
		return characteristicsQuantity;
	}

	public void setCharacteristicsQuantity(List<CharacteristicQuantity> characteristicsQuantity) {
		this.characteristicsQuantity = characteristicsQuantity;
	}

	public String getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(String studentGroup) {
		this.studentGroup = studentGroup;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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

	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
}