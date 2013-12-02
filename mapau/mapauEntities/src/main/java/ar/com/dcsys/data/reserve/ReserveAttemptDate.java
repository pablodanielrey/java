package ar.com.dcsys.data.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;

public interface ReserveAttemptDate {

	public ReserveAttemptDate getRelated();
	public void setRelated(ReserveAttemptDate related);

	public Person getCreator();
	public void setCreator(Person creator);

	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public String getDescription();
	public void setDescription(String description);

	public Course getCourse();
	public void setCourse(Course course);

	public List<Person> getRelatedPersons();
	public void setRelatedPersons(List<Person> relatedPersons);

	public ReserveAttemptDateType getType();
	public void setType(ReserveAttemptDateType type);

	public List<CharacteristicQuantity> getCharacteristicsQuantity();
	public void setCharacteristicsQuantity(List<CharacteristicQuantity> characteristicsQuantity);

	public String getStudentGroup();
	public void setStudentGroup(String studentGroup);

	public Area getArea();
	public void setArea(Area area);

	public String getId();
	public void setId(String id);

	public Date getStart();
	public void setStart(Date start);
	
	public Date getEnd();
	public void setEnd(Date end);

	public Long getVersion();
	public void setVersion(Long version);
	
}
