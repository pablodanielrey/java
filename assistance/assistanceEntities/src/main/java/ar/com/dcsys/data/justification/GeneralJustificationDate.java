package ar.com.dcsys.data.justification;

import java.util.Date;

public interface GeneralJustificationDate {

	public String getId();
	public void setId(String id);
	
	public Justification getJustification();
	public void setJustification(Justification justification);

	public Date getStart();
	public void setStart(Date start);

	public Date getEnd();
	public void setEnd(Date end);	
	
	public String getNotes();
	public void setNotes(String notes);
	
}
