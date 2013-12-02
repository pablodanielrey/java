package ar.com.dcsys.data.reserve;


public interface ReserveAttemptDateType {

	public String getName();
	public void setName(String name);

	public String getDescription();
	public void setDescription(String description);

	public void setId(String id);
	public String getId();

	public Long getVersion();
	public void setVersion(Long version);
	
}
