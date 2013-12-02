package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.UUID;

public class SubjectBean implements Serializable, Subject {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Long version = 1l;
	private String name;
	
	public void setId(String id) {
		if (id == null) {
			this.id = null;
			return ;
		}
		this.id = UUID.fromString(id);
	}
	
	public String getId() {
		if (this.id == null) {
			return null;
		}
		return this.id.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		String string = "";
		string = string.concat("subject id: " + this.getId() + " ");
		string = string.concat("subject name: " + this.getName() + " ");
		string = string.concat("subject version: " + this.getVersion() + " ");
		
		return string;
	}

}
