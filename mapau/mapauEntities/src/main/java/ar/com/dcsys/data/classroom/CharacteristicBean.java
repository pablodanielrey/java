package ar.com.dcsys.data.classroom;

import java.io.Serializable;
import java.util.UUID;


public class CharacteristicBean implements Serializable, Characteristic {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	private String name;
	private Boolean deleted = false;
	private Long version = 1l;

	public void setId(String id) {
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
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
