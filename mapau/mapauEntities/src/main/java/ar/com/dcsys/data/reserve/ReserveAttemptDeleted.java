package ar.com.dcsys.data.reserve;


public interface ReserveAttemptDeleted {
	
	public Long getVersion();
	public void setVersion(Long version);

	public String getId();	
	public void setId(String id);
	
	public ReserveAttempt getReserveAttempt();	
	public void setReserveAttempt(ReserveAttempt reserveAttempt);
	
	public String getNotes();	
	public void setNotes(String notes);
	
	public String getDescription();	
	public void setDescription(String description);
	
}
