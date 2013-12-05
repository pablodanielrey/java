package ar.com.dcsys.gwt.mapau.client.ui.calendar;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.Calendar;

public class MyCalendar extends Calendar {

	private Appointment app;
	
	@Override
	public void setRollbackAppointment(Appointment appt) {
		app = appt;
		super.setRollbackAppointment(appt);
	}

	@Override
	public void fireUpdateEvent(Appointment appointment) {
		fireEvent(new MyUpdateEvent(appointment,app));
		super.fireUpdateEvent(appointment);
	}
	
}
