package ar.com.dcsys.group.entities.types;

import java.io.Serializable;

public class GroupType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public GroupType() {
		
	}
	
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
	
	/// la igualdad de esta clase y sus subclases están dadas por el nombre de la clase solamente.
	
	@Override
	public int hashCode() {
		return this.getClass().getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return this.getClass().getName().equals(obj.getClass().getName());
	}
	
}
