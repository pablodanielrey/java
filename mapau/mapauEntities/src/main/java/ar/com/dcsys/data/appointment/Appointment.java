package ar.com.dcsys.data.appointment;

import java.util.Date;
import java.util.List;

public interface Appointment {

	public String getId();
	public void setId(String id);
	
	public List<String> getRelatedAppointments();
	public void setRelatedAppointments(List<String> relatedAppointments);
	
	public String getCourseId();
	public void setCourseId(String id);
	public String getCourseName();
	public void setCourseName(String name);

	public String getStudentGroup();
	public void setStudentGroup(String sg);
	
	public String getClassRoomId();
	public void setClassRoomId(String id);
	public String getClassRoomName();
	public void setClassRoomName(String name);
	
	public String getAreaId();
	public void setAreaId(String id);
	
	public String getRaTypeId();
	public void setRaTypeId(String id);
	public String getRaTypeName();
	public void setRaTypeName(String raTypeName);
	
	public Date getStart();
	public void setStart(Date start);
	public Date getEnd();
	public void setEnd(Date end);
	
	public List<String> getRelatedPersonsIds();
	public void setRelatedPersonsIds(List<String> personsIds);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Boolean getVisible();
	public void setVisible(Boolean t);
	
	public void setRaId(String raId);
	public String getRaId();
	
	public String getRadId();
	public void setRadId(String radId);
	
	public String getrId();
	public void setrId(String rId);
	
	
}
