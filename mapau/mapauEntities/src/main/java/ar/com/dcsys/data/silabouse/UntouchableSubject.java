package ar.com.dcsys.data.silabouse;

import java.util.List;


public interface UntouchableSubject {
	
	public void setId(String id);	
	public String getId();
	
	public Long getVersion();	
	public void setVersion(Long version);
	
	public void setCourses(List<Course> courses);	
	public List<Course> getCourses();
	
	public void setArea(Area area);	
	public Area getArea();

}
