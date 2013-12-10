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
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyAppointmentEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyDragAndDropAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyDragAndDropAppointmentEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ThisOrRelatedEvent;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CreateModifyAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update.CreateModifyAppointmentsViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update.CreateModifyAppointmentsViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.ResettableEventBus;


public class ModifyAppointmentActivity extends AbstractActivity implements CreateModifyAppointmentsView.Presenter {
	
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
	
	
	public ModifyAppointmentActivity(AppointmentsManager appointmentsManager, 
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
		
		this.eventBus.addHandler(ModifyAppointmentEvent.TYPE, new ModifyAppointmentEventHandler() {
			@Override
			public void onModifyAppointment(ModifyAppointmentEvent event) {

				MapauAppointment mapp = event.getAppointment();
				if (mapp == null) {
					return;
				}
		
				view.setPresenter(ModifyAppointmentActivity.this);
				view.setAreasSelectionModel(areaSelection);
				view.setCoursesSelectionModel(courseSelection);
				view.setTypesSelection(typeSelection);
				
				start = event.getStart();
				end = event.getEnd();
				receiver = event.getReceiver();

				if (appointmentsManager.isWorking(mapp)) {
					appointment = mapp;
				} else {
					appointment = mapp.clone();
				}
				startEdit();
			}
		});
		
		this.eventBus.addHandler(ModifyDragAndDropAppointmentEvent.TYPE, new ModifyDragAndDropAppointmentEventHandler() {
			@Override
			public void onModifyDragAndDropEvent(ModifyDragAndDropAppointmentEvent event) {
				
				MapauAppointment mapp = event.getAppointment();
				if (mapp == null) {
					return;
				}

				if (mapp.getClassRoom() != null) {
					showMessage("Para poder modificar las fechas de una reserva debe primero eliminar la asignación de aula");
					return;
				}				

				view.setPresenter(ModifyAppointmentActivity.this);
				view.setAreasSelectionModel(areaSelection);
				view.setCoursesSelectionModel(courseSelection);
				view.setTypesSelection(typeSelection);
				
				start = event.getStart();
				end = event.getEnd();
				receiver = event.getReceiver();

				if (appointmentsManager.isWorking(mapp)) {
					appointment = mapp;
				} else {
					appointment = mapp.clone();
				}
				
				startDragAndDropEdit();
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
				view.setStudentGroup(appointment.getStudentGroup());				
				
				CreateModifyAppointmentsUtils.findAreas(rf, new Receiver<List<Area>>() {
					@Override
					public void onSuccess(List<Area> areas) {
						
						view.setAreas(areas);
						
						CreateModifyAppointmentsUtils.findCharacteristics(cf, new Receiver<List<Characteristic>>() {
							@Override
							public void onSuccess(List<Characteristic> chars) {
								view.setCharacteristics(chars);
								
								CreateModifyAppointmentsUtils.findTypes(rf, new Receiver<List<ReserveAttemptDateType>>() {
									@Override
									public void onSuccess(List<ReserveAttemptDateType> types) {
										
										view.setTypes(types);
										
										CreateModifyAppointmentsUtils.findCourses(rf, new Receiver<List<Course>>() {
											@Override
											public void onSuccess(List<Course> courses) {
												
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
	
	private void startEdit() {
		view.clear();
		setViewPermissions();
		setAppointmentData();
		findAllRequiredData();
		showView();
	}
	
	private void startDragAndDropEdit() {
		view.clear();
		setAppointmentData();
		persist();
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
		
	}
	
	
	private CharacteristicQuantity getUsersCount(List<CharacteristicQuantity> chars) {
		if (chars == null) {
			return null;
		}
		for (CharacteristicQuantity c : chars) {
			if (c.getCharacteristic().getName().equalsIgnoreCase("Capacidad")) {
				return c;
			}
		}
		return null;
	}
	
	@Override
	public void persist() {
		
		
		// HORRIBLE PER OMAS SEGURO ASI NO SE MANDAN CAGADAS.
		if (appointment.getClassRoom() != null) {
			showMessage("No se puede modificar una reserva que tenga aula asignada");
			return;
		}
		
		//TODO: precondiciones que hay que chequear
		
		// calculo los deltas y si son diferentes de 0 entonces chequeo el aula.
		final long startDelta = view.getStart().getTime() - appointment.getStart().getTime();
		final long endDelta = view.getEnd().getTime() - appointment.getEnd().getTime();		
		
		if ((startDelta != 0l || endDelta != 0l) && appointment.getClassRoom() != null) {
			showMessage("Para poder modificar las fechas de una reserva debe primero eliminar la asignación de aula");
			return;
		}
		
		List<CharacteristicQuantity> chars = view.getCharacteristicsQuantity();
		final CharacteristicQuantity c1 = getUsersCount(chars);
		CharacteristicQuantity c2 = getUsersCount(appointment.getCharacteristics());
		if (c1 != null && c2 != null && (!(c1.getQuantity().equals(c2.getQuantity())))) {
			showMessage("Para poder modificar la cantidad de personas de una reserva debe primero eliminar la asignación de aula");
			return;
		}

		
		
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
		
		
		eventBus.fireEvent(new ThisOrRelatedEvent(new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean andRelated) {
				
				if (andRelated) {
					
					// tengo que obtener todos los relacionados.
					Date date = appointment.getStart();
					eventBus.fireEvent(new GenerateRepetitionEvent(date, new Receiver<GenerateRepetitionView.RepetitionData>() {
						@Override
						public void onSuccess(GenerateRepetitionView.RepetitionData data) {

							List<Date> dates = data.dates;
							
							// obtengo todo slos relacionados dados por la serie del appointment original.
							appointmentsManager.findAllAppointmentsBy(appointment, dates, true, new Receiver<List<MapauAppointment>>() {
								@Override
								public void onSuccess(List<MapauAppointment> related) {

									
									if (related != null) {
										
										DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
										StringBuilder sb = new StringBuilder();
										boolean error = false;
										for (MapauAppointment app : related) {
											
											if (app.getClassRoom() != null) {
												
												// si tiene aula asignada entonces chequea las precondiciones de que no se modifique ni las fechas ni la cantidad de personas.
												
												if (startDelta != 0 || endDelta != 0) {
													error = true;
													Date date = app.getStart();
													sb.append(dateFormat.format(date)).append("\n");
												} else {
													CharacteristicQuantity c2 = getUsersCount(app.getCharacteristics());
													if ((c1 != null) && (c2 != null) && (!(c1.getQuantity().equals(c2.getQuantity())))) {
														error = true;
														Date date = app.getStart();
														sb.append(dateFormat.format(date)).append("\n");
													}
												}
											}
										}
										if (error) {
											hideView();
											String message = "No se puede modificar ya que las fechas\n" + sb.toString() + "contienen asignaciónes de aulas";
											receiver.onFailure(new Throwable(message));
											resetSessionData();
											return;
										}
									}
									
																		
									
									List<MapauAppointment> relatedAppointments = new ArrayList<MapauAppointment>();
									// tenog todos los relacionados. asi que los modifico con respecto a las fechas.
									if (related != null) {
										for (MapauAppointment r : related) {
											
											MapauAppointment relatedAppointment;
											if (appointmentsManager.isWorking(r)) {
												relatedAppointment = r;
											} else {
												relatedAppointment = r.clone();
											}
											
											Date newStart = new Date(relatedAppointment.getStart().getTime() + startDelta);
											Date newEnd = new Date(relatedAppointment.getEnd().getTime() + endDelta);

											relatedAppointment.setStart(newStart);
											relatedAppointment.setEnd(newEnd);
											relatedAppointment.setStudentGroup(view.getStudentGroup());
											
											if (view.getRelatedPersons() != null) {
												relatedAppointment.setRelatedPersons(view.getRelatedPersons());
											}
											
											if (view.getCharacteristicsQuantity() != null) {
												relatedAppointment.setCharacteristics(view.getCharacteristicsQuantity());
											}
											
											relatedAppointment.setCourse(course);
											relatedAppointment.setType(type);
											relatedAppointment.setArea(area);																
											
											relatedAppointments.add(relatedAppointment);
										}
									}
									
									// modifico el appointment original
									if (!appointmentsManager.isWorking(appointment)) {
										appointment = appointment.clone();
									}
									
									appointment.setStart(new Date(appointment.getStart().getTime() + startDelta));
									appointment.setEnd(new Date(appointment.getEnd().getTime() + endDelta));
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

									relatedAppointments.add(appointment);
									
									
									for (MapauAppointment r : relatedAppointments) {
										if (State.NEW.equals(r.getState())) {
											r.setState(State.NEW);
										} else {
											r.setState(State.MODIFIED);
										}
										appointmentsManager.add(r);
									}
									
									receiver.onSuccess(relatedAppointments);
									resetSessionData();
									hideView();
									
								};
								@Override
								public void onFailure(Throwable error) {
									hideView();
									receiver.onFailure(error);
									resetSessionData();
								};
							});							
						}
						@Override
						public void onFailure(Throwable error) {
							hideView();
							receiver.onFailure(error);
							resetSessionData();
						}
					}));
					
				} else {
					// no los relacionados, solo el appointmnet original
					// Date start, Date end, y el mapauAppointment a modificar.
					
					// modifico el appointment original
					if (appointmentsManager.isServer(appointment)) {
						appointment = appointment.clone();
					}
					
					appointment.setStart(new Date(appointment.getStart().getTime() + startDelta));
					appointment.setEnd(new Date(appointment.getEnd().getTime() + endDelta));
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
					
					if (State.NEW.equals(appointment.getState())) {
						appointment.setState(State.NEW);
					} else {
						appointment.setState(State.MODIFIED);
					}					
					appointmentsManager.add(appointment);

					receiver.onSuccess(Arrays.asList(appointment));
					resetSessionData();
					hideView();					
				}
			}
			@Override
			public void onFailure(Throwable error) {
				hideView();
				receiver.onFailure(error);
				resetSessionData();
			}
		}));

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
