package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.group.Group;

public class AreaBean implements Serializable, Area {
	
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Long version = 1l;
	private List<Course> courses = new ArrayList<Course>();
	private List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
	private String name;
	private Group group;
	
	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	public String getId() {
		if (this.id == null) {
			return null;
		} else {
			return this.id.toString();
		}
	}
	
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	public List<ClassRoom> getClassRooms() {
		return classRooms;
	}

	public void setClassRooms(List<ClassRoom> classRooms) {
		this.classRooms = classRooms;
	}
	
}