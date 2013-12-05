package ar.com.dcsys.data.appointment;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;

public interface AppointmentV2 {
	public String getId();
	public void setId(String id);
	
	public List<String> getRelatedAppointments();
	public void setRelatedAppointments(List<String> relatedAppointments);

	public ReserveAttemptDateType getRaType();
	public void setRaType(ReserveAttemptDateType raType);
	
	public ReserveAttemptDate getRad();
	public void setRad(ReserveAttemptDate rad);

	public Reserve getR();
	public void setR(Reserve r);

	public Person getOwner();
	public void setOwner(Person p);
	
	public List<Person> getRelatedPersons();
	public void setRelatedPersons(List<Person> relatedPersons);
	
	public List<CharacteristicQuantity> getCharacteristics();
	public void setCharacteristics(List<CharacteristicQuantity> chars);

	public ClassRoom getClassRoom();
	public void setClassRoom(ClassRoom classRoom);

	public Course getCourse();
	public void setCourse(Course course);

	public String getStudentGroup();
	public void setStudentGroup(String studentGroup);

	public Area getArea();
	public void setArea(Area area);

	public Date getStart();
	public void setStart(Date start);

	public Date getEnd();
	public void setEnd(Date end);

	public String getDescription();
	public void setDescription(String description);

	public Boolean getVisible();
	public void setVisible(Boolean visible);	
}
