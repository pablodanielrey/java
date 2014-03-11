package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.UUID;

public class JustificationBean implements Serializable, Justification {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String description;
	private UUID id;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}

}
