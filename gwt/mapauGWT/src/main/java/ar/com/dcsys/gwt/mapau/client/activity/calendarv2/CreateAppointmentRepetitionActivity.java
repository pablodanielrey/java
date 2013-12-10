package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentRepetitionEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepetitionData;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;


public class CreateAppointmentRepetitionActivity extends AbstractActivity {

	private final AppointmentsManager appointmentsManager;
	private ResettableEventBus eventBus;
	
	public CreateAppointmentRepetitionActivity(AppointmentsManager appointmentsManager) {
		this.appointmentsManager = appointmentsManager;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		this.eventBus = new ResettableEventBus(eventBus);
		
		this.eventBus.addHandler(CreateAppointmentRepetitionEvent.TYPE, new CreateAppointmentRepetitionEventHandler() {
			@Override
			public void onCreateAppointmentRepetition(CreateAppointmentRepetitionEvent event) {
								
				Receiver<List<MapauAppointment>>receiver = event.getReceiver();
				MapauAppointment mapp = event.getAppointment();
				
				createRepetition(mapp, receiver);
				
			}
		});
		
	}
	
	/**
	 * llama a la pantalla de generación de repeticiones y con esas fechas retornadas genera los MapauAppointments.
	 * @param mapp
	 */
	private void createRepetition(final MapauAppointment mapp, final Receiver<List<MapauAppointment>> receiver) {
		
		Date date = mapp.getStart();
		eventBus.fireEvent(new GenerateRepetitionEvent(date, new Receiver<RepetitionData>() {
			@Override
			public void onSuccess(RepetitionData data) {
				List<Date> dates = data.dates;
				
				final List<MapauAppointment> appointments = new ArrayList<MapauAppointment>();
				if (dates == null || dates.size() <= 0) {
					receiver.onSuccess(appointments);
					return;
				}
				
				for (Date date : dates) {
					MapauAppointment newMapp = mapp.clone();
					
					// lo transformo en un nuevo Appointment.
					newMapp.setId(MapauAppointment.getNewId());
					newMapp.setAppointment(null);
					newMapp.setClassRoom(null);
					newMapp.setState(State.NEW);
					
					adjustDates(newMapp,date);
					
					appointments.add(newMapp);
					appointmentsManager.add(newMapp);
				}
				
				receiver.onSuccess(appointments);
			}
			@Override
			public void onFailure(Throwable t) {
			}
		}));
		
	}
	
	/*
	 * ajusta la fecha de incio y de fin del appointment para que se corresponda con la fecha pasada por parámetro.
	 * la hora la mantiene igual a laque tiene el appointment.
	 */
	private void adjustDates(MapauAppointment mapp, Date date) {
		
		int day = date.getDate();
		int month = date.getMonth();
		int year = date.getYear();
		
		Date start = mapp.getStart();
		start.setDate(day);
		start.setMonth(month);
		start.setYear(year);
		
		mapp.setStart(start);
		
		Date end = mapp.getEnd();
		end.setDate(day);
		end.setMonth(month);
		end.setYear(year);
		
		mapp.setEnd(end);
	}
	
	
	@Override
	public void onStop() {
		eventBus.removeHandlers();
	}

}
