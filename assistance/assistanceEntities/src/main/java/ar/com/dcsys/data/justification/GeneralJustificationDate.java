package ar.com.dcsys.data.justification;

import java.io.Serializable;
import java.util.Date;

public class GeneralJustificationDate  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private Justification justification;
	private Date start;
	private Date end;
	private String notes;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
