package ar.com.dcsys.data.person.types;

import java.io.Serializable;

public class PersonType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public String getId() {
		return getClass().getName();
	}
	
	public Long getVersion() {
		return 1l;
	}
	
	public String getDescription() {
		return "";
	}
	
	public void setDescription(String desc) {
		// nada;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof PersonType) {
			return ((PersonType)obj).getId().equals(getId());
		}
		
		return false;
	}

}
