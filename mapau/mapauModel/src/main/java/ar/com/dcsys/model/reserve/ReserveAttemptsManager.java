package ar.com.dcsys.model.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttempt;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDeleted;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.utils.DatesRange;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public interface ReserveAttemptsManager {
	
	
	///////// filtros ////////////
	
	public List<TransferFilterType> findAllFilters() throws MapauException;
	public List<ReserveAttemptDate> findReserveAttemptDateBy(Date start, Date end, List<TransferFilter> filters) throws MapauException;
	public List<ReserveAttemptDate> findReserveAttemptDateBy(Date start, Date end) throws MapauException;
	
	public List<Appointment> findAppointmentsBy(Date start, Date end, List<TransferFilter> filters) throws MapauException;
	public List<Appointment> findAppointmentsBy(List<TransferFilter> filters) throws MapauException;
	public List<AppointmentV2> findAppointmentsV2By(List<TransferFilter> filters) throws MapauException;
	public List<AppointmentV2> findAppointmentsV2By(Date start, Date end,List<TransferFilter> filters) throws MapauException;
//	public void createNew(List<Appointment> appointments) throws MapauException;
	public void createNewAppointments(List<AppointmentV2> appointments) throws MapauException;
	public void deleteAppointment(AppointmentV2 app) throws MapauException;
	public void modify(AppointmentV2 a) throws MapauException, PersonException;
//	public void modify(Appointment a) throws MapauException, PersonException, AuthException;
	public void createReserve(Appointment a, List<ClassRoom> classRooms) throws MapauException, PersonException;
	public void deleteReserves(Appointment a) throws MapauException, PersonException;
	
	
	///////// classrooms //////
	
	public List<AppointmentV2> findAllAppointmentsBy(AppointmentV2 app, List<Date> dates, boolean checkHour) throws MapauException;
	public List<ReserveAttemptDate> findAllAppointmentsBy(ReserveAttemptDate app, List<Date> dates, boolean checkHour) throws MapauException;
	public List<ClassRoom> findAllClassRoomsAvailableIn(List<ReserveAttemptDate> apps) throws MapauException;
	public List<ClassRoom> findAllClassRoomsAvailableIn(List<ReserveAttemptDate> apps, boolean checkCapacity) throws MapauException;

	
	////////  operación de pedido de reserva ////////
	
	public List<Course> getCoursesToCreateReserveAttempt() throws MapauException;
	public Boolean checkDateAvailable(ReserveAttemptDate date) throws MapauException;
	public List<ReserveAttemptDate> checkDateAvailable(List<ReserveAttemptDate> date) throws MapauException;

	//////////////////////////////////////////////////
	
	
	public void setVisible(ReserveAttemptDate rad) throws MapauException;
	public void setVisible(ReserveAttemptDate rad, List<Group> groups) throws MapauException;
	public void removeVisible(ReserveAttemptDate rad) throws MapauException;
		

	//////////////// operacion de confirmacion de visibilidad //////////////
	
	
	public List<ReserveAttempt> getReserveAttemptsToConfirmReserve() throws MapauException;

	///// modificaciones varias //////
	
	public String persist(ReserveAttemptDate date) throws MapauException;
	
	
	//////////////////////////////////////////////////////////
	
	public ReserveAttemptDate findReserveAttemptDateById(String id) throws MapauException;
	
	public String persist(ReserveAttemptDeleted rd) throws MapauException;
	public ReserveAttemptDeleted findReserveAttemptDeletedById(String id) throws MapauException, PersonException;
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////// RESERVES /////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	public Reserve createNewReserve(ReserveAttemptDate date, ClassRoom classRoom, String description) throws MapauException;
	public List<Reserve> findAllCollidingWith(Date start, Date end, List<ClassRoom> classRooms) throws MapauException;
	public List<Reserve> findReservesBy(ReserveAttemptDate date) throws MapauException;
	
	
	public String persist(Reserve reserve) throws MapauException;
	public void remove(Reserve reserve) throws MapauException;
	public Reserve findReserveById(String id) throws MapauException;
	public List<Reserve> findAllReservesByDates(Date start, Date end) throws MapauException;
	
	
	/**
	 * Obtiene todas las Reservas realizadas que colisionen en algún punto con las fechas indicadas.
	 * 
	 * @param rad
	 * @param dates
	 * @return
	 * @throws MapauException
	 */
	public List<Reserve> findAllCollidingWith(List<DatesRange> dates) throws MapauException;	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////// CHARACTERISTICS /////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void persist(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void remove(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void removeAllCharacteristics(ReserveAttemptDate reserveAttemptDate) throws MapauException;	
	
}
