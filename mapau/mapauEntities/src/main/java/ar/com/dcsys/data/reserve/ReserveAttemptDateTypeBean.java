package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.UUID;

public class ReserveAttemptDateTypeBean implements Serializable, ReserveAttemptDateType {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Long version = 1l;
	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
	
	
}
