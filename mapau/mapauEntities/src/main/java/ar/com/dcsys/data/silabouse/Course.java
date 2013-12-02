package ar.com.dcsys.data.silabouse;


public interface Course extends AssignableUnit {

	public void setId(String id);	
	public String getId();

	public Subject getSubject();
	public void setSubject(Subject s);
	
	public String getName();	
	public void setName(String name);
	
	public Long getVersion();
	public void setVersion(Long version);
	
}
