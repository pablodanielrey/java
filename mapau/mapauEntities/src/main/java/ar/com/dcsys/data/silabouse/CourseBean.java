package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.UUID;

public class CourseBean implements Serializable, Course {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	private Long version = 1l;
	private String name;
	private Subject subject;
	
	public void setId(String id){
		if (id == null) {
			this.id = null;
			return ;
		}
		this.id = UUID.fromString(id);
	}
	
	public String getId(){
		if (this.id == null) {
			return null;
		} else {
			return this.id.toString();
		}
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject s) {
		subject = s;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
	public Long getVersion(){
		return version;
	}

	public void setVersion(Long version){
		this.version = version;
	}
	
	@Override
	public String toString() {
		String string = "";
		string = string.concat("course id: " + this.getId() + " ");
		string = string.concat("course name: " + this.getName() + " ");
		string = string.concat("course version: " + this.getVersion() + " ");
		if (this.getSubject() != null) {
			string = string.concat(this.getSubject().toString());
		} else {
			string = string.concat("subject is null");
		}
		
		return string;
	}
}
