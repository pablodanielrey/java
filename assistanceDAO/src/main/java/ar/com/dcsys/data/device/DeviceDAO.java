package ar.com.dcsys.data.device;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.DeviceException;

public interface DeviceDAO extends Serializable {

	public Device findById(String id) throws DeviceException;
	
	public List<Device> findAll() throws DeviceException;
	
	public String persist(Device d) throws DeviceException;
	public void remove(Device d) throws DeviceException;
	
}
