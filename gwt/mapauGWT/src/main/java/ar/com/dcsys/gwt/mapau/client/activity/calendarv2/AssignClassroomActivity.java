package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.AssignClassroomEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.AssignClassroomEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.AssignClassroomView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepetitionData;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.assign.AssignClassroomViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.assign.AssignClassroomViewResources;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessageView;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessageViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessageViewResources;
import ar.com.dcsys.utils.ClassRoomSort;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;


public class AssignClassroomActivity extends AbstractActivity implements AssignClassroomView.Presenter {

	private ResettableEventBus eventBus;
	private MapauAppointment appointment;
	private final List<ClassRoom> assignedClassRooms = new ArrayList<ClassRoom>(); 
	
	private Receiver<List<MapauAppointment>> receiver;
	private final List<Date> dates = new ArrayList<Date>();
	private final MultiSelectionModel<ClassRoom> classRoomSelection;
	private final List<ReserveAttemptDate> appointmentsToReserve = new ArrayList<ReserveAttemptDate>();
	 
	private final MapauManager rf;
	private final AppointmentsManager appointmentsManager;
	private final AssignClassroomView view;
	private final UserValidationMessageView viewUserValidation;
	
	private void finalizeTransaction() {
		dates.clear();
		
		classRoomSelection.clear();
		appointmentsToReserve.clear();
		
		appointment = null;
		assignedClassRooms.clear();
		receiver = null;
		
		hideView();
	}
	
	
	private final AssignClassroomEventHandler handler = new AssignClassroomEventHandler() {
		@Override
		public void onAssignClassroomEvent(AssignClassroomEvent event) {
			receiver = event.getReceiver();
			appointment = event.getAppointment();
			assignedClassRooms.clear();
			
			// precondiciones del appointment
			if (!(State.UNTOUCHED.equals(appointment.getState()))) {
				showMessage("Debe guardar los cambios antes de poder asignar aula");
				receiver.onFailure(new Throwable("Debe guardar los cambios antes de asignar aula"));
				finalizeTransaction();
				return;
			}

			// busco las reservas que ya tengo asignadas a ese RAD para obtener las aulas asignadas.
			rf.findReservesBy(appointment.getAppointment().getRad(), new Receiver<List<Reserve>>() {
				@Override
				public void onSuccess(List<Reserve> reserves) {
					classRoomSelection.clear();
					
					if (reserves != null) {
						for (Reserve r : reserves) {
							ClassRoom c = r.getClassRoom();
							assignedClassRooms.add(c);
						}
					}
					
					view.clear();
					view.setClassRooms(null);
					showView();
					
					viewUserValidation.clear();
					viewUserValidation.setMessage("Atención: usted esta asignando un aula con menor capacidad de la necesaria.");
					viewUserValidation.setConfirmationLabel("Confirmo la asignación del aula a pesar de tener menor capacidad");
					
					updateClassRooms();
				}
				@Override
				public void onFailure(Throwable error) {
					receiver.onFailure(error);
					finalizeTransaction();
					return;
				};
			});
		}
	};
	
	
	private final Receiver<RepetitionData> generateRepetition = new Receiver<RepetitionData>() {
		@Override
		public void onSuccess(RepetitionData data) {
			List<Date> dates = data.dates;
			if (dates != null && dates.size() > 0) {
				AssignClassroomActivity.this.dates.clear();
				AssignClassroomActivity.this.dates.addAll(dates);
				updateClassRooms();
			}
			
			if (data.descripcion != null) {
				view.setRepetitionSummary(data.descripcion);
			}
		}
		@Override
		public void onFailure(Throwable t) {
		}
	};
	
	/*
	 * Atributo que implementa la interface userValidationMessageView.Presenter
	 */
	
	private final UserValidationMessageView.Presenter userValidationMessageActivity = new UserValidationMessageView.Presenter() {
			
		@Override
		public void commit() {
			Set<ClassRoom> classroomsSelected = classRoomSelection.getSelectedSet();
			AssignClassroomActivity.this.commit(classroomsSelected);
			view.hideValidationMessage();
		}

		@Override
		public void cancel() {
			view.hideValidationMessage();
		}
		
	};
	
	
	public AssignClassroomActivity(MapauManager rf,
								   AppointmentsManager aManager,
								   AssignClassroomView view, 
								   UserValidationMessageView viewUserValidation) {
		this.rf = rf;
		this.appointmentsManager = aManager;
		this.view = view;
		this.viewUserValidation = viewUserValidation;
		classRoomSelection = new MultiSelectionModel<ClassRoom>();
	}
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);
		this.eventBus.addHandler(AssignClassroomEvent.TYPE, handler);
		
		AcceptsOneWidget container = view.getUserValidationContainer();
		container.setWidget(viewUserValidation);
		this.viewUserValidation.setPresenter(userValidationMessageActivity);

		UserValidationMessageViewCss styleUserValidation = UserValidationMessageViewResources.INSTANCE.style(); 
		styleUserValidation.ensureInjected();			
		
		view.setPresenter(this);
		view.setClassRoomsSelectionModel(classRoomSelection);
		
		AssignClassroomViewCss style = AssignClassroomViewResources.INSTANCE.style(); 
		style.ensureInjected();			
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.clear();
		view.setClassRooms(null);
		view.setClassRoomsSelectionModel(null);
		
		this.eventBus.removeHandlers();
		this.eventBus = null;
	}

	@Override
	public void generateRepetition() {
		Date date = appointment.getStart();
		eventBus.fireEvent(new GenerateRepetitionEvent(date,generateRepetition));
	}

	protected void commit(Set<ClassRoom> classroomsSelected) {
		List<ClassRoom> classrooms = new ArrayList<ClassRoom>();
		classrooms.addAll(classroomsSelected);	

		rf.createReserves(appointmentsToReserve, classrooms, "", new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				receiver.onSuccess(null);
				finalizeTransaction();
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
				finalizeTransaction();
			}
		});
	}
	
	@Override
	public void commit() {
		
		Set<ClassRoom> selection = classRoomSelection.getSelectedSet();	
		if (selection.size() <= 0) {
			commit(selection);
			return;
		}
		long totalCapacity = 0;
		for (ClassRoom cr : selection) {
			List <CharacteristicQuantity> cqs = cr.getCharacteristicQuantity();
			if (cqs == null || cqs.size() <= 0) {
				continue;
			}
			
			for (CharacteristicQuantity cq : cqs) {
				
				Characteristic characteristic = cq.getCharacteristic();
				
				if (characteristic.getName().equals("Capacidad")) {
					
					if (cq.getQuantity() != null) {
						totalCapacity += cq.getQuantity();						
					}
					break;
					
				}
			}
			
		}
		
		long capacity = 0l;
		List<CharacteristicQuantity>  characteristics = appointment.getCharacteristics();
		for (CharacteristicQuantity cq : characteristics) {
			Characteristic characteristic = cq.getCharacteristic();
			if (characteristic.getName().equals("Capacidad")) {
				capacity = cq.getQuantity(); 
				break;
			}
		}
		
		if (totalCapacity < capacity) {
			view.showValidationMessage();
		} else {
			commit(selection);
		}
		
		
		/*
		
		List<Reserve> reserves = new ArrayList<Reserve>();
		ReserveRequest rr = rf.reserveRequest();
		for (ReserveAttemptDate rad : appointmentsToReserve) {
			
			for (ClassRoom c : selection) {
				Reserve reserve = rr.create(Reserve.class);
				reserve.setReserveAttemptDate(rad);
				reserve.setClassRoom(c);
				reserves.add(reserve);
			}
		}
		
		rr.persist(reserves).with("reserveAttemptDate","classRoom").fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void v) {
				receiver.onSuccess(null);
				finalizeTransaction();
			}
			@Override
			public void onFailure(Throwable error) {
				receiver.onFailure(error);
				finalizeTransaction();
			}
		});
		*/

	}

	@Override
	public void cancel() {
		receiver.onSuccess(new ArrayList<MapauAppointment>());
		hideView();
	}
	
	private void showView() {
		eventBus.fireEvent(new ShowViewEvent(AssignClassroomView.class.getName()));
	}
	
	private void hideView() {
		eventBus.fireEvent(new HideViewEvent(AssignClassroomView.class.getName()));
	}
	
	
	
	@Override
	public void findClassroom() {
		updateClassRooms();		
	}
	
	
	/**
	 * Busco las aulas disponibles para ese appointment. Si tiene fechas generadas 
	 * por la repetición tambien se hace búsqueda por esas fechas.
	 * tambien se realiza buscando o no el horario igual o fehcas solamente. 
	 */
	private void updateClassRooms() {
		
		if (view == null) {
			return;
		}
		
		if (appointment == null) {
			return;
		}
		
		boolean checkHour = view.getSameHours();
		AppointmentV2 app = appointment.getAppointment();
		
		rf.findAllAppointmentsBy(app, dates, checkHour, new Receiver<List<AppointmentV2>>() {
			@Override
			public void onSuccess(List<AppointmentV2> rads) {
				
				appointmentsToReserve.clear();
				appointmentsToReserve.add(appointment.getAppointment().getRad());
				
				if (rads != null) {
					for (AppointmentV2 rad : rads) {
						appointmentsToReserve.add(rad.getRad());
					}
				}
				
				boolean checkCapacity = view.getCheckCapacity();
				
				rf.findAllClassRoomsAvailableIn(appointmentsToReserve, checkCapacity, new Receiver<List<ClassRoom>>() {
					@Override
					public void onSuccess(List<ClassRoom> classRooms) {
						classRoomSelection.clear();
						
						List<ClassRoom> crs = new ArrayList<ClassRoom>();
						if (classRooms != null) {
							crs.addAll(classRooms);
						}
						crs.addAll(assignedClassRooms);
						ClassRoomSort.sort(crs);
						view.setClassRooms(crs);
						
						for (ClassRoom c : assignedClassRooms) {
							classRoomSelection.setSelected(c, true);
						}
					}
					
					@Override
					public void onFailure(Throwable error) {
						receiver.onFailure(error);
						finalizeTransaction();						
					}
				});
			}
			
			@Override
			public void onFailure(Throwable error) {
				receiver.onFailure(error);
				finalizeTransaction();
			}
		});
		
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
}
