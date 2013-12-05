package ar.com.dcsys.data.appointment;

import java.io.Serializable;
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

public class AppointmentV2Bean  implements Serializable, AppointmentV2 {

	private static final long serialVersionUID = 1L;

	
	/*
	 * los campos id y realatedAppointmens es valido solo dentro de la consulta
	 * O sea no son persistentes en la base ni sirven para identificar cierto apointment en el lado del servidor!!!.
	 * son para facilidades graficas.
	 */
	
	private String id;
	private List<String> relatedAppointments;
	
	////////////////
	
	private ReserveAttemptDateType raType;
	private ReserveAttemptDate rad;
	private Reserve r;
	
	private Person owner;

	private List<Person> relatedPersons;
	private List<CharacteristicQuantity> characteristics;
	
	private ClassRoom classRoom;
	private Course course;
	private String studentGroup;
	private Area area;
	
	private Date start;
	private Date end;
	
	private String description;

	private Boolean visible;
	
	public AppointmentV2Bean() {
		
	}

	@Override
	public String getId() {
		return id;
	}

	@Override	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public List<String> getRelatedAppointments() {
		return relatedAppointments;
	}

	@Override
	public void setRelatedAppointments(List<String> relatedAppointments) {
		this.relatedAppointments = relatedAppointments;
	}

	@Override
	public ReserveAttemptDateType getRaType() {
		return raType;
	}

	@Override
	public void setRaType(ReserveAttemptDateType raType) {
		this.raType = raType;
	}

	@Override
	public ReserveAttemptDate getRad() {
		return rad;
	}

	@Override
	public void setRad(ReserveAttemptDate rad) {
		this.rad = rad;
	}

	@Override
	public Reserve getR() {
		return r;
	}

	@Override
	public void setR(Reserve r) {
		this.r = r;
	}

	@Override
	public List<Person> getRelatedPersons() {
		return relatedPersons;
	}

	@Override
	public void setRelatedPersons(List<Person> relatedPersons) {
		this.relatedPersons = relatedPersons;
	}
	

	@Override
	public List<CharacteristicQuantity> getCharacteristics() {
		return characteristics;
	}

	@Override
	public void setCharacteristics(List<CharacteristicQuantity> characteristics) {
		this.characteristics = characteristics;
	}

	@Override
	public Person getOwner() {
		return owner;
	}

	@Override
	public void setOwner(Person owner) {
		this.owner = owner;
	}	

	@Override
	public ClassRoom getClassRoom() {
		return classRoom;
	}

	@Override
	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	@Override
	public Course getCourse() {
		return course;
	}

	@Override
	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String getStudentGroup() {
		return studentGroup;
	}

	@Override
	public void setStudentGroup(String studentGroup) {
		this.studentGroup = studentGroup;
	}

	@Override
	public Area getArea() {
		return area;
	}

	@Override
	public void setArea(Area area) {
		this.area = area;
	}

	@Override
	public Date getStart() {
		return start;
	}

	@Override
	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public Date getEnd() {
		return end;
	}

	@Override
	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	

}
