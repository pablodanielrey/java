package ar.com.dcsys.data.device;

import java.io.Serializable;
import java.util.UUID;

public class DeviceBean  implements Serializable, Device {

	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private String name;
	private String description;

	private String mac; 
	private String ip;
	private String netmask;
	private String gateway;

	private Boolean enabled;
	
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getNetmask() {
		return netmask;
	}
	
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	
	public String getGateway() {
		return gateway;
	}
	
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
}
