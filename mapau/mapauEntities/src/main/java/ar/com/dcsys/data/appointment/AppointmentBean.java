package ar.com.dcsys.data.appointment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AppointmentBean implements Serializable, Appointment {

	private static final long serialVersionUID = 1L;

	
	/*
	 * los campos id y realatedAppointmens es valido solo dentro de la consulta
	 * O sea no son persistentes en la base ni sirven para identificar cierto apointment en el lado del servidor!!!.
	 * son para facilidades graficas.
	 */
	
	private String id;
	private List<String> relatedAppointments;
	
	////////////////
	
	
	private String raId;
	private String radId;
	private String rId;
	
	private List<String> relatedPersonsIds;
	
	private String classRoomId;
	private String classRoomName;
	private String courseId;
	private String courseName;
	private String studentGroup;
	private String raTypeId;
	private String raTypeName;
	private String areaId;
	
	private Date start;
	private Date end;
	
	private String description;


	private Boolean visible;
	
	
	
	public AppointmentBean() {
		
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
	public String getRaId() {
		return raId;
	}


	@Override
	public void setRaId(String raId) {
		this.raId = raId;
	}


	@Override
	public String getRadId() {
		return radId;
	}


	@Override
	public void setRadId(String radId) {
		this.radId = radId;
	}


	@Override
	public String getrId() {
		return rId;
	}


	@Override
	public void setrId(String rId) {
		this.rId = rId;
	}


	@Override
	public List<String> getRelatedPersonsIds() {
		return relatedPersonsIds;
	}


	@Override
	public void setRelatedPersonsIds(List<String> relatedPersonsIds) {
		this.relatedPersonsIds = relatedPersonsIds;
	}


	@Override
	public String getClassRoomId() {
		return classRoomId;
	}


	@Override
	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	@Override
	public String getCourseId() {
		return courseId;
	}


	@Override
	public void setCourseId(String courseId) {
		this.courseId = courseId;
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
	public Boolean getVisible() {
		return visible;
	}


	@Override
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}


	@Override
	public String getRaTypeId() {
		return raTypeId;
	}


	@Override
	public void setRaTypeId(String raTypeId) {
		this.raTypeId = raTypeId;
	}


	@Override
	public String getAreaId() {
		return areaId;
	}


	@Override
	public void setAreaId(String areaId) {
		this.areaId = areaId;
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
	public String getClassRoomName() {
		return classRoomName;
	}


	@Override
	public void setClassRoomName(String classRoomName) {
		this.classRoomName = classRoomName;
	}


	@Override
	public String getCourseName() {
		return courseName;
	}


	@Override
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	@Override
	public String getRaTypeName() {
		return raTypeName;
	}


	@Override
	public void setRaTypeName(String raTypeName) {
		this.raTypeName = raTypeName;
	}

}
