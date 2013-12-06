package ar.com.dcsys.data.assignment;

import java.util.Date;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;

public interface Assignment {

	public void setId(String id);
	public String getId();

	public String getType();
	public void setType(String type);
	
	public Long getVersion();
	public void setVersion(Long version);

	public Person getPerson();
	public void setPerson(Person person);
	
	public Course getCourse();
	public void setCourse(Course course);

	public Assignment getRelatedAssignment();
	public void setRelatedAssignment(Assignment relatedAssignment);
	
	public String getNotes();
	public void setNotes(String notes);

	public Date getFrom();
	public void setFrom(Date from);

	public Date getTo();
	public void setTo(Date to);

}
