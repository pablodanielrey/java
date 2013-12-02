package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.UUID;

public class ReserveAttemptDeletedBean implements Serializable, ReserveAttemptDeleted {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private ReserveAttempt reserveAttempt;
	private String notes;
	private String description;
	private Long version = 1l;
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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
	
	public ReserveAttempt getReserveAttempt() {
		return reserveAttempt;
	}
	
	public void setReserveAttempt(ReserveAttempt reserveAttempt) {
		this.reserveAttempt = reserveAttempt;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}