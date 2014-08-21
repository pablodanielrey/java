package ar.com.dcsys.model.log;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;


public interface AttLogsManager {

	public List<String> findAll() throws AttLogException;
	public List<AttLog> findAll(Date start, Date end) throws AttLogException, PersonException;
	public List<AttLog> findAll(Person person, Date start, Date end) throws AttLogException;
	public AttLog findById(String id) throws AttLogException, PersonException;
	
	public Boolean findByExactDate(Person person, Date date) throws AttLogException, PersonException, DeviceException;
	
	public void persist(AttLog log) throws AttLogException;
	
	
//	public List<RawAttLogValue> findLogsToSynchronize(Device d) throws AttLogException, DeviceException;
//	public void synchronizeLogs(Device d) throws AttLogException;
//	public void persist(RawAttLogValue log) throws AttLogException, PersonException, DeviceException;
}
