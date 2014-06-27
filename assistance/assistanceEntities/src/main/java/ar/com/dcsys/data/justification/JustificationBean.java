package ar.com.dcsys.data.justification;


public class JustificationBean implements Justification {

	private String code;
	private String description;
	private String id;

	public JustificationBean() {
		
	}
	
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
