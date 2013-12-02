package ar.com.dcsys.data.reserve;

import ar.com.dcsys.exceptions.MapauException;

public interface Visible {

	public void setId(String id);
	public String getId();
	
	public Long getVersion();
	public void setVersion(Long version);

	public ReserveAttemptDate getDate();
	public void setDate(ReserveAttemptDate date);
	
	public Boolean isVisible() throws MapauException;

}
