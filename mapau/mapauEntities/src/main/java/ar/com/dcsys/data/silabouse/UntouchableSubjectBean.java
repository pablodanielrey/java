package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UntouchableSubjectBean implements Serializable, UntouchableSubject {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Long version = 1l;
	private List<Course> courses = new ArrayList<Course>();
	private Area area;
	
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
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public Area getArea() {
		return area;
	}

}