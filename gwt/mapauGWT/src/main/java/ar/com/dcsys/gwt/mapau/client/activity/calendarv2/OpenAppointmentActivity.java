package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.AssignClassroomEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.DeleteAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.OpenAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.OpenAppointmentEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView.Operation;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.open.OpenAppointmentViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.open.OpenAppointmentViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class OpenAppointmentActivity extends AbstractActivity implements OpenAppointmentView.Presenter {

	private final OpenAppointmentView view;
	
	private MapauAppointment appointment;
	private Map<Operation,Receiver<List<MapauAppointment>>> handlers;
	private ResettableEventBus eventBus;
	
	public OpenAppointmentActivity(OpenAppointmentView view) {
		this.view = view;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);

		this.eventBus.addHandler(OpenAppointmentEvent.TYPE, new OpenAppointmentEventHandler() {
			@Override
			public void onOpenAppointment(OpenAppointmentEvent event) {
				MapauAppointment mapp = event.getMapp(); 
				if (mapp == null) {
					return;
				}
				
				appointment = mapp;
				handlers = event.getHandlers();

				view.setOperations(extractOperations(handlers));
				view.setAppointment(appointment);
				showView();
			}
		});
		

		OpenAppointmentViewCss style = OpenAppointmentViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		view.setPresenter(this);
		
		panel.setWidget(view);		
	}
	
	
	private List<Operation> extractOperations(Map<Operation,Receiver<List<MapauAppointment>>> handlers) {
		List<Operation> operations = new ArrayList<Operation>();
		operations.addAll(handlers.keySet());
		return operations;
	}
	
	
	@Override
	public void onStop() {
		eventBus.removeHandlers();

		view.clear();
		view.setPresenter(null);
	}
	
	private void showView() {
		eventBus.fireEvent(new ShowViewEvent(OpenAppointmentView.class.getName()));
	}
	
	private void hideView() {
		eventBus.fireEvent(new HideViewEvent(OpenAppointmentView.class.getName()));
	}	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	

	/**
	 * Es nesaria esta clase para limpiar las variables mantenidas por el activity ya que se hacen las llamadas disparando eventos
	 * por lo que no se puede encadenar dentro del tratamiento de un evento ya disparado en el mismo bus.
	 * @author pablo
	 *
	 */
	private class CallFinalizer implements Receiver<List<MapauAppointment>> {
		
		private final Receiver<List<MapauAppointment>> receiver;
		
		public CallFinalizer(Receiver<List<MapauAppointment>> rec) {
			this.receiver = rec;
		}
		
		@Override
		public void onSuccess(List<MapauAppointment> mapps) {
			receiver.onSuccess(mapps);
			handlers = null;
			appointment = null;
		}
		@Override
		public void onFailure(Throwable error) {
			handlers = null;
			appointment = null;
			receiver.onFailure(error);
		}
	}
	
	
	/**
	 * Inicia una operación disparando el evento indicado.
	 * se debe realizar con scheduleDeferred porque dispara un evento.
	 */
	@Override
	public void startOperation(Operation op) {
		
		if (appointment == null) {
			return;
		}		
		
		hideView();
		
		final Receiver<List<MapauAppointment>> handler = handlers.get(op);
		
		if (Operation.DELETE.equals(op)) {

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					eventBus.fireEvent(new DeleteAppointmentEvent(appointment, new CallFinalizer(handler)));
				}
			});

		} else if (Operation.EDIT.equals(op)) {

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					Date start = appointment.getStart();
					Date end = appointment.getEnd();
					
					eventBus.fireEvent(new ModifyAppointmentEvent(start, end, appointment, new CallFinalizer(handler)));
				}
			});

		} else if (Operation.REPEAT.equals(op)) {
			
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					eventBus.fireEvent(new CreateAppointmentRepetitionEvent(appointment, new CallFinalizer(handler)));
				}
			});
			
		} else if (Operation.ASSIGN.equals(op)) {

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					eventBus.fireEvent(new AssignClassroomEvent(appointment, new CallFinalizer(handler)));
				}
			});
			
			
		} else {
			showMessage("Todavia no implementado");
			appointment = null;
			handlers = null;
		}
		
		
		return;		
	}

}
