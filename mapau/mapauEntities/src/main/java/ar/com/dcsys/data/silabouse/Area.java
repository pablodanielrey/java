package ar.com.dcsys.data.silabouse;

import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;


public interface Area {
	
	public void setId(String id);	
	public String getId();
	
	public Long getVersion();
	public void setVersion(Long version);
	
	public List<Course> getCourses();
	public void setCourses(List<Course> courses);

	public String getName();
	public void setName(String name);
	
	public Group getGroup();
	public void setGroup(Group group);

	public List<ClassRoom> getClassRooms();
	public void setClassRooms(List<ClassRoom> classRooms);
	
}
