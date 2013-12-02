package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.UUID;

import ar.com.dcsys.exceptions.MapauException;

public class VisibleBean implements Serializable, Visible{

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Long version;
	private ReserveAttemptDate date;
	
	public void setId(String id){
		this.id = UUID.fromString(id);
	}
	
	public String getId(){
		if (this.id == null) {
			return null;
		}
		return this.id.toString();
	}	
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public ReserveAttemptDate getDate() {
		return date;
	}

	public void setDate(ReserveAttemptDate date) {
		this.date = date;
	}

	public Boolean isVisible() throws MapauException {
		return true;
	}

}
