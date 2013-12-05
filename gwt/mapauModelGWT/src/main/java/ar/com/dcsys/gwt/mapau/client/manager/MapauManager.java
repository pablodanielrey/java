package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface MapauManager {

	public void createNewAppointments(List<AppointmentV2> apps, Receiver<Void> rec);
	public void deleteAppointment(AppointmentV2 app, Receiver<Void> rec);
	public void modify(AppointmentV2 app, Receiver<Void> rec);
	
	public void findAllAppointmentsBy(AppointmentV2 appv2, List<Date> dates, boolean checkHour, Receiver<List<AppointmentV2>> rec);
	public void findAllFilters(Receiver<List<TransferFilterType>> rec);
	public void findAppointmentsV2By(List<TransferFilter> filters, Receiver<List<AppointmentV2>> rec);
	public void findAppointmentsBy(List<TransferFilter> filters, Receiver<List<Appointment>> rec);
	
}