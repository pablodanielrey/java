package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class GeneralJustificationDateBean  implements Serializable, GeneralJustificationDate {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private Justification justification;
	private Date start;
	private Date end;
	private String notes;
	

	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}	
	
	public Justification getJustification() {
		return justification;
	}

	public void setJustification(Justification justification) {
		this.justification = justification;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
		

}
