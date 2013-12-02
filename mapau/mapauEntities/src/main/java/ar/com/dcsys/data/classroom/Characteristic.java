package ar.com.dcsys.data.classroom;


public interface Characteristic {

	public void setId(String id);
	public String getId();
	
	public String getName();
	public void setName(String name);
	
	public Boolean getDeleted();
	public void setDeleted(Boolean deleted);
	
	public Long getVersion();
	public void setVersion(Long version);
	
}
