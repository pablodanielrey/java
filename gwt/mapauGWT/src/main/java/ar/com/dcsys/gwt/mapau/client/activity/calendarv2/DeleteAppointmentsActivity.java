package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.DeleteAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.DeleteAppointmentEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.DeleteAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.delete.DeleteAppointmentsViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.delete.DeleteAppointmentsViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.AcceptsOneWidget;



public class DeleteAppointmentsActivity extends AbstractActivity implements DeleteAppointmentsView.Presenter {

	private final DeleteAppointmentsView view;
	private final AppointmentsManager appointmentsManager;
	private ResettableEventBus eventBus;
	
	public DeleteAppointmentsActivity(AppointmentsManager appointmentsManager, DeleteAppointmentsView view) {
		this.view = view;
		this.appointmentsManager = appointmentsManager;
	}
	
	private MapauAppointment appointment;
	private Receiver<List<MapauAppointment>> receiver;
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		this.eventBus = new ResettableEventBus(eventBus);
		this.eventBus.addHandler(DeleteAppointmentEvent.TYPE, new DeleteAppointmentEventHandler() {
			@Override
			public void onDeleteAppointment(DeleteAppointmentEvent event) {

				appointment = event.getAppointment();
				receiver = event.getReceiver();
				
				if (State.DELETED.equals(appointment.getState())) {
					receiver.onFailure(new Throwable("Ya se encuentra pendiente de eliminación"));
					appointment = null;
					receiver = null;
					return;
				}
				
				
				if (appointment.getClassRoom() != null) {
					receiver.onFailure(new Throwable("Debe eliminar la asignación del aula antes de poder eliminar el horario de la reserva"));
					appointment = null;
					receiver = null;
					return;
				}				
			
				view.clear();
				view.setAppointment(appointment);
				
				showView();
			}
		});
		
		DeleteAppointmentsViewCss style = DeleteAppointmentsViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		view.setPresenter(this);
		
		panel.setWidget(view);
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}	
	
	
	private void showView() {
		eventBus.fireEvent(new ShowViewEvent(DeleteAppointmentsView.class.getName()));
	}
	
	private void hideView() {
		eventBus.fireEvent(new HideViewEvent(DeleteAppointmentsView.class.getName()));
	}
	
	@Override
	public void onlyOne() {
		hideView();
		
		if (receiver == null) {
			showMessage("receiver == null");
			return;
		}
		
		if (appointment == null) {
			showMessage("appointment == null");
			return;
		}

		deleteAppointment(appointment);

		receiver.onSuccess(Arrays.asList(appointment));
		appointment = null;
		receiver = null;
	}
	
	
	private void deleteAppointment(MapauAppointment appointment) {
		
		if (appointmentsManager.isServer(appointment)) {
			appointment = appointment.clone();
			appointment.setState(State.DELETED);	
			appointmentsManager.add(appointment);
			
		} else if (appointmentsManager.isWorking(appointment)) {

			if (State.NEW.equals(appointment.getState())) {
				appointmentsManager.remove(appointment);
			} else {			
				appointment.setState(State.DELETED);
			}
			
		}
	}
	
	
	
	@Override
	public void relatedAlso() {
		hideView();
		
		if (receiver == null) {
			showMessage("receiver == null");
			return;
		}
		
		if (appointment == null) {
			showMessage("appointment == null");
			return;
		}
		
		Date date = appointment.getStart();
		
		eventBus.fireEvent(new GenerateRepetitionEvent(date, new Receiver<GenerateRepetitionView.RepetitionData>() {
			@Override
			public void onSuccess(GenerateRepetitionView.RepetitionData data) {
				List<Date> dates = data.dates;
				
				appointmentsManager.findAllAppointmentsBy(appointment, dates, true, new Receiver<List<MapauAppointment>>() {
					@Override
					public void onSuccess(List<MapauAppointment> related) {

						List<MapauAppointment> relatedAppointments = new ArrayList<MapauAppointment>();
						relatedAppointments.add(appointment);
						
						if (related != null) {
							
							// chequeo la precodición de que no tengan aula asignada.
							DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
							StringBuilder sb = new StringBuilder();
							boolean found = false;
							for (MapauAppointment app : related) {
								if (app.getClassRoom() != null) {
									found = true;
									Date date = app.getStart();
									sb.append(dateFormat.format(date)).append("\n");
								}
							}
							if (found) {
								hideView();
								String message = "No se puede eliminar ya que las fechas\n" + sb.toString() + "contienen asignaciónes de aulas";
								receiver.onFailure(new Throwable(message));
								appointment = null;
								receiver = null;
								return;
							}
							
							relatedAppointments.addAll(related);
						}
						
						for (MapauAppointment app : relatedAppointments) {
							deleteAppointment(app);
						}
						
						receiver.onSuccess(relatedAppointments);
						appointment = null;
						receiver = null;
					}
					@Override
					public void onFailure(Throwable error) {
						receiver.onFailure(error);
						appointment = null;
						receiver = null;
						return;
					}
				});
				
			}
			@Override
			public void onFailure(Throwable error) {
				receiver.onFailure(error);
				appointment = null;
				receiver = null;
				return;
			};
		}));
					
	}
	
	@Override
	public void cancel() {
		view.clear();
		hideView();
		receiver.onSuccess(new ArrayList<MapauAppointment>());
		appointment = null;
		receiver = null;
	}
	
	@Override
	public void onStop() {
		appointment = null;
		receiver = null;
		
		eventBus.removeHandlers();

		view.clear();
		view.setPresenter(null);
	}

}
