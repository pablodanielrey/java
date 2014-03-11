package ar.com.dcsys.data.log;

import java.util.Date;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.person.Person;

public interface AttLog {

	public String getId();
	public void setId(String id);
	
	public Device getDevice();
	public void setDevice(Device device);

	public Person getPerson();
	public void setPerson(Person person);
	
	public Date getDate();
	public void setDate(Date date);

	public Long getVerifyMode();
	public void setVerifyMode(Long verifyMode);

}
