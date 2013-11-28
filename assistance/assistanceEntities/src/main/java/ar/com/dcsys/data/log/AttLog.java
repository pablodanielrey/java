package ar.com.dcsys.data.log;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.person.Person;

public class AttLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	
	private Device device;
	private Person person;
	
	private Date date;
	private Long verifyMode;
	
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getVerifyMode() {
		return verifyMode;
	}

	public void setVerifyMode(Long verifyMode) {
		this.verifyMode = verifyMode;
	}

}
