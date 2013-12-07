package ar.com.dcsys.gwt.mapau.shared.list;

import java.util.List;

import ar.com.dcsys.data.appointment.Appointment;

public interface AppointmentList {

	public void setList(List<Appointment> l);
	public List<Appointment> getList();
	
}
