package ar.com.dcsys.model.device;

import java.util.List;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;

public interface DevicesManager {

	public List<String> findAll() throws DeviceException;
	public Device findById(String id) throws DeviceException;
	public void persist(Device d) throws DeviceException;
	
	public void enroll(String personId) throws PersonException, DeviceException;
	
}
