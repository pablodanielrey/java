package ar.com.dcsys.data.device;

public interface Device {

	public String getId();
	public void setId(String id);
	
	public String getName();
	public void setName(String name);
	
	public String getMac();
	public void setMac(String mac);
	
	public String getIp();
	public void setIp(String ip);
	
	public String getNetmask();
	public void setNetmask(String netmask);
	
	public String getGateway();
	public void setGateway(String gateway);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Boolean getEnabled();
	public void setEnabled(Boolean enabled);
	
}
