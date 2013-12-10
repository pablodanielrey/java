package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface MapauManager {

	public void createNewAppointments(List<AppointmentV2> apps, Receiver<Void> rec);
	public void deleteAppointment(AppointmentV2 app, Receiver<Void> rec);
	public void modifyAppointment(AppointmentV2 app, Receiver<Void> rec);
	
	public void findAllAppointmentsBy(AppointmentV2 appv2, List<Date> dates, boolean checkHour, Receiver<List<AppointmentV2>> rec);
	public void findAllFilters(Receiver<List<TransferFilterType>> rec);
	public void findAppointmentsV2By(List<TransferFilter> filters, Receiver<List<AppointmentV2>> rec);
	public void findAppointmentsBy(List<TransferFilter> filters, Receiver<List<Appointment>> rec);
	
	
	public void findReservesBy(ReserveAttemptDate rad, Receiver<List<Reserve>> rec);
	public void createReserves(List<ReserveAttemptDate> rads, List<ClassRoom> classRooms, Receiver<Void> rec);
	public void findAllClassRoomsAvailableIn(List<ReserveAttemptDate> rads, Boolean checkCapacity, Receiver<List<ClassRoom>> rec);
	public void getCoursesToCreateReserve(Receiver<List<Course>> rec);
	public void findAllReserveAttemptType(Receiver<List<ReserveAttemptDateType>> rec);
	
	public void findAllArea(Receiver<List<Area>> rec);
	
}
