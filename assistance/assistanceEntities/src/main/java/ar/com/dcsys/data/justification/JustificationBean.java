package ar.com.dcsys.data.justification;

import java.io.Serializable;


public class JustificationBean implements Justification,Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String description;
	private String id;
	
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
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
