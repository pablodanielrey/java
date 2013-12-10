package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CreateModifyAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update.CreateModifyAppointmentsViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update.CreateModifyAppointmentsViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.ResettableEventBus;


public class CreateAppointmentActivity extends AbstractActivity implements CreateModifyAppointmentsView.Presenter {
	
	private final CreateModifyAppointmentsView view;
	private final MapauManager rf;
	private final SilegManager sf;
	private final ClassRoomsManager cf;
	private final AppointmentsManager appointmentsManager;
	private ResettableEventBus eventBus;

	private Date start;
	private Date end;
	private MapauAppointment appointment;
	private Receiver<List<MapauAppointment>> receiver;
	
	private final SingleSelectionModel<Course> courseSelection;
	private final SingleSelectionModel<ReserveAttemptDateType> typeSelection;
	private final SingleSelectionModel<Area> areaSelection;
	
	
	private final Handler courseSelectionHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Course course = courseSelection.getSelectedObject();
			if (course == null) {
				view.setPersons(new ArrayList<Person>());
			} else {
				CreateModifyAppointmentsUtils.findPersons(sf, course, new Receiver<List<Person>>() {
					@Override
					public void onSuccess(List<Person> persons) {
						view.setPersons(persons);
					}
					@Override
					public void onFailure(Throwable error) {
						showMessage(error.getMessage());
					}
				});
			}
		}
	}; 
	
	
	
	//////////////////////
	
	
	public CreateAppointmentActivity(AppointmentsManager appointmentsManager, 
									 MapauManager rf,
									 SilegManager sf,
									 ClassRoomsManager cf,
									 CreateModifyAppointmentsView view) {
		this.view = view;
		this.rf = rf;
		this.sf = sf;
		this.cf = cf;
		this.appointmentsManager = appointmentsManager;
		
		courseSelection = new SingleSelectionModel<Course>();
		courseSelection.addSelectionChangeHandler(courseSelectionHandler);
		
		typeSelection = new SingleSelectionModel<ReserveAttemptDateType>();
		areaSelection = new SingleSelectionModel<Area>();
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);


		
		CreateModifyAppointmentsViewCss style = CreateModifyAppointmentsViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		this.eventBus.addHandler(CreateAppointmentEvent.TYPE, new CreateAppointmentEventHandler() {
			@Override
			public void onCreateAppointment(CreateAppointmentEvent event) {
				
				view.setPresenter(CreateAppointmentActivity.this);
				view.setAreasSelectionModel(areaSelection);
				view.setCoursesSelectionModel(courseSelection);
				view.setTypesSelection(typeSelection);				
				
				
				start = event.getDate();
				end = new Date(start.getTime() + 3600000l);
				receiver = event.getReceiver();
				appointment = new MapauAppointment();
				startCreate();
			}
		});
		
		panel.setWidget(view);
	}
	

	@Override
	public void onStop() {
		eventBus.removeHandlers();
		
		view.setAreasSelectionModel(null);
		view.setCoursesSelectionModel(null);
		view.setTypesSelection(null);		
		view.clear();
		view.setPresenter(null);
	}

	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	
	private void selectCourseInView(Course course) {
		if (course == null) {
			courseSelection.clear();
			return;
		}
		
		Course course2 = courseSelection.getSelectedObject();
		if (course.equals(course2)) {
			courseSelectionHandler.onSelectionChange(null);
		} else {
			courseSelection.setSelected(course, true);
		}
	}
	
	
	/**
	 * Setea las personas relacionadas con el appointment dentro de la vista.
	 * estas personas deben estar dentro de la colleción que se pasa como parametro.
	 * 
	 * @param persons, lista de personas posibles a ser seleccionadas como relacionadas de la reserva.
	 */
	private void setSelectedPersons(List<Person> persons) {
		List<Person> selectedPersons = new ArrayList<Person>();
		if (appointment != null) {
			List<Person> apersons = appointment.getRelatedPersons();
			if (apersons != null) {
				for (Person p : apersons) {
					if (persons.contains(p)) {
						selectedPersons.add(p);
					}
				}
			}
		}
		view.setRelatedPersons(selectedPersons);
	}

	
	/**
	 * Busca y setea en la vista todos los datos requeridos para poder hacer una reserva.
	 * Las personas se buscan cuando se selecciona un curso. se realiza dentro del selection model de cursos.
	 */
	private void findAllRequiredData() {

		CreateModifyAppointmentsUtils.findStudentGroups(new Receiver<List<String>>() {
			@Override
			public void onSuccess(List<String> sg) {
				view.setStudentGroups(sg);
				
				CreateModifyAppointmentsUtils.findAreas(rf, new Receiver<List<Area>>() {
					@Override
					public void onSuccess(List<Area> areas) {
						if (areas != null && areas.size() > 0) {
							areaSelection.setSelected(areas.get(0), true);
						}
						view.setAreas(areas);
						
						CreateModifyAppointmentsUtils.findCharacteristics(cf, new Receiver<List<Characteristic>>() {
							@Override
							public void onSuccess(List<Characteristic> chars) {
								view.setCharacteristics(chars);
								
								CreateModifyAppointmentsUtils.findTypes(rf, new Receiver<List<ReserveAttemptDateType>>() {
									@Override
									public void onSuccess(List<ReserveAttemptDateType> types) {
										if (types != null && types.size() > 0) {
											typeSelection.setSelected(types.get(0), true);
										}
										view.setTypes(types);
										
										CreateModifyAppointmentsUtils.findCourses(rf, new Receiver<List<Course>>() {
											@Override
											public void onSuccess(List<Course> courses) {
												if (courses != null && courses.size() > 0) {
													selectCourseInView(courses.get(0));
												}
												view.setCourses(courses);
												
											}
											@Override
											public void onFailure(Throwable error) {
												showMessage(error.getMessage());
											}
										});
									}
									@Override
									public void onFailure(Throwable error) {
										showMessage(error.getMessage());
									}
								});
							}
							@Override
							public void onFailure(Throwable error) {
								showMessage(error.getMessage());
							}
						});
						
					}
					@Override
					public void onFailure(Throwable error) {
						showMessage(error.getMessage());
					}
				});
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
			}
		});
	}
	

	private void startCreate() {
		view.clear();
		setViewPermissions();
		setAppointmentData();
		findAllRequiredData();
		showView();
	}
	
	private void setViewPermissions() {
		view.setAreaReadOnly(false);
		view.setCourseReadOnly(false);
		view.setStudentGroupReadOnly(false);
		view.setTypeReadOnly(false);
		view.setCharacteristicsReadOnly(false);
		view.setRelatedPersonsReadOnly(false);
		view.setNotesReadOnly(false);
		view.setClassRoomReadOnly(false);
		view.setVisibleReadOnly(false);
	}
	
	private void setAppointmentData() {
		view.setAppointment(appointment);
		view.setStart(start);
		view.setEnd(end);
		
		List<CharacteristicQuantity> chars = appointment.getCharacteristics();
		view.setCharacteristicQuantity(chars);
		
		Course course = appointment.getCourse();
		selectCourseInView(course);
		
		Area area = appointment.getArea();
		areaSelection.clear();
		if (area != null) {
			areaSelection.setSelected(area, true);
		}
		
		ReserveAttemptDateType type = appointment.getType();
		typeSelection.clear();
		if (type != null) {
			typeSelection.setSelected(type, true);
		}
		
		setSelectedPersons(new ArrayList<Person>());
	}
	
	
	@Override
	public void persist() {
		
		final Course course = courseSelection.getSelectedObject();

		if (course == null) {
			showMessage("Seleccione un curso");
			return;
		}

		final ReserveAttemptDateType type = typeSelection.getSelectedObject();
		if (type == null) {
			showMessage("Seleccione un tipo de reserva");
			return;
		}
		
		final Area area = areaSelection.getSelectedObject();
		if (area == null) {
			showMessage("Seleccione algun área al que se le realiza el pedido");
			return;
		}
		
		
		Date start = view.getStart();
		Date end = view.getEnd();
		if (end.before(start)) {
			showMessage("La fecha de fin debe ser posterior a la fecha de inicio");
			return;
		}
		
		appointment.setStart(start);
		appointment.setEnd(end);
		appointment.setStudentGroup(view.getStudentGroup());
		
		if (view.getRelatedPersons() != null) {
			appointment.setRelatedPersons(view.getRelatedPersons());
		}
		
		if (view.getCharacteristicsQuantity() != null) {
			appointment.setCharacteristics(view.getCharacteristicsQuantity());
		}
		
		appointment.setCourse(course);
		appointment.setType(type);
		appointment.setArea(area);					
		appointment.setState(State.NEW);

		appointmentsManager.add(appointment);

		receiver.onSuccess(Arrays.asList(appointment));
		resetSessionData();
		hideView();						

	}
	
	
	private void resetSessionData() {
		appointment = null;
		start = null;
		end = null;
		receiver = null;
	}
	
	@Override
	public void cancel() {
		view.clear();
		
		courseSelection.clear();
		typeSelection.clear();
		areaSelection.clear();
	
		resetSessionData();
		hideView();
	}

	private void showView() {
		eventBus.fireEvent(new ShowViewEvent(CreateModifyAppointmentsView.class.getName()));
	}

	private void hideView() {
		eventBus.fireEvent(new HideViewEvent(CreateModifyAppointmentsView.class.getName()));
	}
	
}
