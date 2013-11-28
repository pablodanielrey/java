package ar.com.dcsys.data.log;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;

public interface AttLogDAO extends Serializable {

	public List<AttLog> findAll(Date start, Date end) throws AttLogException, PersonException;
	public List<AttLog> findAll(Person person, Date start, Date end) throws AttLogException;
	public AttLog findById(String id) throws AttLogException, PersonException;
	
	public Boolean findByExactDate(Person person, Date date) throws AttLogException, PersonException, DeviceException;
	
	public void persist(AttLog log) throws AttLogException;
	
	public interface Params {
		public Person findPersonById(String person_id) throws PersonException;
		public Device findDeviceById(String device_id) throws DeviceException;
	}
	
}
