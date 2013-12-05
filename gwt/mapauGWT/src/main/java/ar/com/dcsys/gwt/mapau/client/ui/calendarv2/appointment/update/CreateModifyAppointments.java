package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.CharacteristicQuantityBean;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CreateModifyAppointmentsView;
import ar.com.dcsys.utils.AreaSort;
import ar.com.dcsys.utils.CoursesSort;
import ar.com.dcsys.utils.ReserveAttemptDateTypeSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class CreateModifyAppointments extends Composite implements CreateModifyAppointmentsView {

	private static CreateModifyAppointmentsUiBinder uiBinder = GWT.create(CreateModifyAppointmentsUiBinder.class);

	interface CreateModifyAppointmentsUiBinder extends UiBinder<Widget, CreateModifyAppointments> {
	}
	
	@UiField CreateModifyAppointmentsViewResources res;

	@UiField Label title;
	@UiField(provided=true) DateBox date;
	@UiField(provided=true) TextBox startDateHours;
	@UiField(provided=true) TextBox startDateMinutes;
	@UiField(provided=true) TextBox endDateHours;
	@UiField(provided=true) TextBox endDateMinutes;
	@UiField(provided=true) ValueListBox<Course> course;
	@UiField ListBox studentGroup;
	@UiField(provided=true) ValueListBox<ReserveAttemptDateType> type;
	@UiField FlowPanel capacityPanel;
	@UiField(provided=true) TextBox capacity;
	@UiField FlowPanel multimediaPanel;
	@UiField(provided=true) CheckBox multimedia;
	@UiField(provided=true) ValueListBox<Area> area;
	
	@UiField TextArea notes;
	@UiField Label cancel;
	@UiField Label commit;
	
	private CreateModifyAppointmentsView.Presenter presenter;
	private MapauAppointment app;
	
	private SingleSelectionModel<Course> courseSelection;
	private SingleSelectionModel<ReserveAttemptDateType> typeSelection;
	private SingleSelectionModel<Area> areaSelection;
	

	private List<Characteristic> characteristics;
	private Map<String,CharacteristicQuantity> appointmentCharacteristics; 
	
	
	/**
	 * Handler que mantiene actualizada la selección de la ValueListBox con los cambios en el selectionModel.
	 */
	private final Handler courseSelectionHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Course selected = courseSelection.getSelectedObject();
			if (selected == null) {
				course.setValue(null);
			} else {
				course.setValue(selected);
			}
		}
	};
	
	
	
	/**
	 * Handler que mantiene actualizado la selección de la ValueListBox con el selectionModel
	 */
	private final ValueChangeHandler<Course> courseValueHandler = new ValueChangeHandler<Course>() {
		@Override
		public void onValueChange(ValueChangeEvent<Course> event) {
			if (courseSelection != null) {
				Course c = course.getValue();
				courseSelection.setSelected(c, true);
			}
		}
	};
	
	/**
	 * Handler que mantiene actualizado la selección de la ValueListBox con el selectionModel
	 */
	private final Handler typesSelectionHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			ReserveAttemptDateType selected = typeSelection.getSelectedObject();
			if (selected == null) {
				type.setValue(null);
			} else {
				type.setValue(selected);
			}
			
		}
	};
	
	/**
	 * Handler que mantiene actualizado la selección de la ValueListBox con el selectionModel
	 */	
	private final ValueChangeHandler<ReserveAttemptDateType> typesValueHandler = new ValueChangeHandler<ReserveAttemptDateType>() {
		@Override
		public void onValueChange(ValueChangeEvent<ReserveAttemptDateType> event) {
			if (typeSelection == null) {
				return;
			}
			ReserveAttemptDateType reserveType = type.getValue();
			typeSelection.setSelected(reserveType, true);
		}
	};
	
	
	/**
	 * Handler para sincronizar la selección del area con el selectionModel.
	 */
	private final Handler areasSelectionHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Area selected = areaSelection.getSelectedObject();
			if (selected == null) {
				area.setValue(null);
			} else {
				area.setValue(selected);
			}
		}
	};
	
	/**
	 * Handler para sincronizar la selección del area con el selectionModel.
	 */
	private final ValueChangeHandler<Area> areasValueHandler = new ValueChangeHandler<Area>() {
		@Override
		public void onValueChange(ValueChangeEvent<Area> event) {
			if (areaSelection ==  null) {
				return;
			}
			Area a = area.getValue();
			areaSelection.setSelected(a, true);
		}
	};
	
	
	/* /////////////////////////////////////////////////////////////////////////////////////////////////////
	 * //////////////////////////////////CREACION E INICIALIZACION////////////////////////////////////////// 
	 */////////////////////////////////////////////////////////////////////////////////////////////////////
	public CreateModifyAppointments() {
		createCharacteristics();
		createCourses();
		createTypes();
		createArea();
		createPersons();
		createDates();
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Crear Reserva");
		characteristics = new ArrayList<Characteristic>();
		appointmentCharacteristics = new HashMap<String,CharacteristicQuantity>();
	}
	
	private void createCharacteristics() {
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Character charCode = event.getCharCode();
				if (!(Character.isDigit(charCode))) {
					TextBox box = (TextBox)event.getSource();
					box.cancelKey();
				}
			}
		};
		
		capacity = new TextBox();
		capacity.setText("0");
		capacity.addKeyPressHandler(handler);
		
		multimedia = new CheckBox();
		multimedia.setValue(false);		
	}
	
	private void createCourses() {		
		
		course = new ValueListBox<Course>(new Renderer<Course>() {
				
				private String getValue(Course course) {
					if (course == null) {
						return "Nulo";
					}
					Subject subject = course.getSubject();
					if (subject != null) {
						String courseName = "";
						if (!course.getName().trim().toLowerCase().equals("original")) {
							courseName = " - " + course.getName().trim();
						}
						return course.getSubject().getName() +  courseName; 
					} else {
						return course.getName();
					}
				}
				
				@Override
				public String render(Course course) {
					return getValue(course);
				}
				
				@Override
				public void render(Course course, Appendable appendable) throws IOException {
					if (course == null) {
						return;
					}
					appendable.append(getValue(course));
				}
		});
		
		course.addValueChangeHandler(courseValueHandler);
		
	}
	
	private void createTypes() {
		type = new ValueListBox<ReserveAttemptDateType>(new Renderer<ReserveAttemptDateType>() {
			
			private String getValue(ReserveAttemptDateType type) {
				if (type == null) {
					return "Nulo";
				}
				return type.getName();
			}
			@Override
			public String render(ReserveAttemptDateType type) {
				return getValue(type);
			}
			
			@Override
			public void render(ReserveAttemptDateType type, Appendable appendable) throws IOException {
				if (type == null) {
					return;
				}
				appendable.append(getValue(type));
			}
		});
		
		type.addValueChangeHandler(typesValueHandler);
	}
	
	private void createArea() {
		area = new ValueListBox<Area>(new Renderer<Area>() {
			
			private String getValue(Area area) {
				if (area == null) {
					return "Nulo";
				}
				return area.getName();
			}
			@Override
			public String render(Area area) {
				return getValue(area);
			}
			
			@Override
			public void render(Area area, Appendable appendable)	throws IOException {
				if (area == null) {
					return;
				}
				appendable.append(getValue(area));
			}
		});
		
		area.addValueChangeHandler(areasValueHandler);
	}
	
	
	private void createDates() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		DatePicker dp = new DatePicker();
		Date today = new Date();
		date = new DateBox(dp, today, new DefaultFormat(dateF));
		
		KeyPressHandler handlerPress = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Character charCode = event.getCharCode();
				if (!(Character.isDigit(charCode))) {
					TextBox box = (TextBox)event.getSource();
					box.cancelKey();
				}
			}
		};

		startDateHours = new TextBox();
		startDateHours.setText("00");
		startDateHours.setMaxLength(2);
		startDateHours.addKeyPressHandler(handlerPress); 		
				
		startDateMinutes = new TextBox();
		startDateMinutes.setText("00");
		startDateMinutes.setMaxLength(2);
		startDateMinutes.addKeyPressHandler(handlerPress);
		
		endDateHours = new TextBox();
		endDateHours.setText("00");
		endDateHours.setMaxLength(2);
		endDateHours.addKeyPressHandler(handlerPress); 		
				
		endDateMinutes = new TextBox();
		endDateMinutes.setMaxLength(2);
		endDateMinutes.setText("00");
		endDateMinutes.addKeyPressHandler(handlerPress);
		
	}
	
	//////////////////////////////FIN DE LA INICIALIZACION Y CREACION////////////////////////////////

	@Override
	public void setAppointment(MapauAppointment a) {
		this.app = a;
		if (a == null) {
			title.setText("Crear Reserva");
			return;
		}
		title.setText("Modificar Reserva");
		setStart(a.getStart());
		setEnd(a.getEnd());
		
		
		List<Person> persons = app.getRelatedPersons();
		selectedPersons.clear();
		if (persons != null && persons.size() > 0) {
			selectedPersons.addAll(persons);
		}
		updatePersonsViewData();	
		
	}
	
	@Override
	public void setStart(Date date) {
		clearStart();
		if (date == null) {
			return;
		}
		this.date.setValue(date);
		int hours = date.getHours();
		int min = date.getMinutes();
		startDateHours.setText(String.valueOf(hours));
		startDateMinutes.setText(String.valueOf(min));
	}
	
	@Override
	public void setEnd(Date date) {
		clearEnd();
		if (date == null) {
			return;
		}
		int hours = date.getHours();
		int min = date.getMinutes();
		endDateHours.setText(String.valueOf(hours));
		endDateMinutes.setText(String.valueOf(min));
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public void setCourses(List<Course> courses) {
		
		if (courseSelection != null) {
			Course selected = courseSelection.getSelectedObject();
			this.course.setValue(selected);
		}
		
		if (courses == null || courses.size() <= 0) {
			courses = new ArrayList<Course>();
		} 
		CoursesSort.sort(courses);
		this.course.setAcceptableValues(courses);
	}

	@Override
	public void setCoursesSelectionModel(SingleSelectionModel<Course> selection) {
		this.courseSelection = selection;
		selection.addSelectionChangeHandler(courseSelectionHandler);
	}

	@Override
	public void setCourseReadOnly(boolean v) {
		setEnabled(course, !v);
	}

	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////// STUDENT GROUP //////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<String> studentGroupData = new ArrayList<String>();
	
	@Override
	public void setStudentGroups(List<String> studentGroups) {
		this.studentGroup.clear();
		studentGroupData.clear();
		if (studentGroups == null || studentGroups.size() <= 0) {
			return;
		}
		
		studentGroupData.addAll(studentGroups);
		
		for (String sg : studentGroups) {
			this.studentGroup.addItem(sg);
		}
		this.studentGroup.setItemSelected(0, true);
	}

	@Override
	public String getStudentGroup() {
		int index = this.studentGroup.getSelectedIndex();
		try {
			String item = studentGroup.getItemText(index);
			return item;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	@Override
	public void setStudentGroup(String studentGroupApp) {
		if (studentGroupApp == null) {
			return;
		}
		
		for ( int i=0; i < studentGroupData.size(); i++) {
			String sg = studentGroupData.get(i);
			if (studentGroupApp.trim().equals(sg.trim())) {
				this.studentGroup.setSelectedIndex(i);
				break;
			}
		}		
	}

	@Override
	public void setStudentGroupReadOnly(boolean v) {
		studentGroup.setEnabled(!v);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	@Override
	public void setTypes(List<ReserveAttemptDateType> types) {
		
		if (typeSelection != null) {
			ReserveAttemptDateType type = typeSelection.getSelectedObject();
			this.type.setValue(type);
		}
		
		if (types == null || types.size() <= 0) {
			types = new ArrayList<ReserveAttemptDateType>();
		}
		ReserveAttemptDateTypeSort.sort(types);
		this.type.setAcceptableValues(types);
	}

	@Override
	public void setTypesSelection(SingleSelectionModel<ReserveAttemptDateType> selection) {
		this.typeSelection = selection;
		selection.addSelectionChangeHandler(typesSelectionHandler);
	}

	
	
	
	@Override
	public void setTypeReadOnly(boolean v) {
		setEnabled(type, !v);
	}

	/**
	 * Setea las caracteristicas posibles.
	 */
	@Override
	public void setCharacteristics(List<Characteristic> chars) {
		
		if (chars == null || chars.size() <= 0) {
			characteristics.clear();
		} else {
			characteristics.addAll(chars);
		}
		
		setVisibleMultimedia(false);
		setVisibleCapacity(false);
		
		for (Characteristic c : chars) {
			if (c.getName().equals("Multimedia")) {
				setVisibleMultimedia(true);
				continue;
			}
			if (c.getName().equals("Capacidad")) {
				setVisibleCapacity(true);
				continue;
			}
		}
	}
	
	private void setVisibleMultimedia(Boolean v) {
		if (v) {
			multimediaPanel.setStyleName(res.style().multimediaPanel());
		} else {
			multimediaPanel.setStyleName(res.style().multimediaHiddenPanel());
		}
	}
	
	private void setVisibleCapacity(Boolean v) {
		if (v) {
			capacityPanel.setStyleName(res.style().capacityPanel());
		} else {
			capacityPanel.setStyleName(res.style().capacityHiddenPanel());
		}
	}

	@Override
	public void setCharacteristicQuantity(List<CharacteristicQuantity> chars) {
		if (chars == null) {
			clearCharacteristics();
			return;
		}
		for (CharacteristicQuantity cq : chars) {
			Characteristic c = cq.getCharacteristic();
			long quantity = cq.getQuantity();
			if (c == null) {
				continue;
			}
			if (c.getName().equals("Multimedia")) {
				appointmentCharacteristics.put("Multimedia", cq);
				multimedia.setValue(quantity > 0);
				continue;
			}
			if (c.getName().equals("Capacidad")) {
				appointmentCharacteristics.put("Capacidad", cq);				
				capacity.setText(String.valueOf(quantity));
				continue;
			}			
		}
	}

	@Override
	public List<CharacteristicQuantity> getCharacteristicsQuantity() {

		List<CharacteristicQuantity> chars = new ArrayList<CharacteristicQuantity>();

		CharacteristicQuantity cq = appointmentCharacteristics.get("Multimedia");
		if (cq == null) {
			for (Characteristic cp : characteristics) {
				if (cp.getName().equals("Multimedia")) {
					cq = new CharacteristicQuantityBean();
					cq.setCharacteristic(cp);
				}
			}
		}
		
		if (cq != null) {
			if (multimedia.getValue()) {
				cq.setQuantity(1l);
			} else {
				cq.setQuantity(0l);
			}
			chars.add(cq);
		}
		
		
		cq = appointmentCharacteristics.get("Capacidad");
		if (cq == null) {
			for (Characteristic cp : characteristics) {
				if (cp.getName().equals("Capacidad")) {
					cq = new CharacteristicQuantityBean();
					cq.setCharacteristic(cp);
				}
			}
		}
		
		if (cq != null) {
			cq.setQuantity(Long.parseLong(capacity.getText()));
			chars.add(cq);
		} 
		
		return chars;
	}

	@Override
	public void setCharacteristicsReadOnly(boolean v) {
		multimedia.setEnabled(!v);
		capacity.setEnabled(!v);
	}

	@Override
	public void setAreas(List<Area> areas) {
		if (areaSelection != null) {
			Area selected = areaSelection.getSelectedObject();
			this.area.setValue(selected);
		}
		
		if (areas == null || areas.size() <= 0) {
			areas = new ArrayList<Area>();
		}
		AreaSort.sort(areas);
		this.area.setAcceptableValues(areas);
	}

	@Override
	public void setAreasSelectionModel(SingleSelectionModel<Area> selection) {
		this.areaSelection = selection;
		selection.addSelectionChangeHandler(areasSelectionHandler);
	}

	
	@Override
	public void setAreaReadOnly(boolean v) {
		setEnabled(area, !v);
	}





	@Override
	public void setNotes(String notes) {
		this.notes.setText(notes);
	}

	@Override
	public String getNotes() {
		return notes.getText();
	}

	@Override
	public void setNotesReadOnly(boolean v) {
		notes.setEnabled(!v);
	}

	@Override
	public void setClassRooms(List<ClassRoom> classRooms) {
		// TODO: por ahora no hay que implementarlo	
		//no implementamos la asignacion de aula todavia
	}

	@Override
	public void setClassRoomsSelectionModel(MultiSelectionModel<ClassRoom> selection) {
		// TODO: por ahora no hay que implementarlo
		//no implementamos la asignacion de aula todavia
	}

	@Override
	public void setClassRoomReadOnly(boolean v) {
		//TODO: no implementamos la asignacion de aula todavia
	}

	@Override
	public Date getStart() {
		Date date = this.date.getValue();
		Integer hours = Integer.parseInt(startDateHours.getText());
		if (hours < 0 || hours >= 24) {
			hours = 0;
			startDateHours.setText("00");
		}
		Integer min = Integer.parseInt(startDateMinutes.getText());
		if (min < 0 || min >= 60) {
			min = 0;
			startDateMinutes.setText("00");
		}
		date.setHours(hours);
		date.setMinutes(min);
		
		return date;
	}

	@Override
	public Date getEnd() {
		Date date = this.date.getValue();
		Integer hours = Integer.parseInt(endDateHours.getText());
		if (hours < 0 || hours >= 24) {
			hours = 0;
			endDateHours.setText("00");
		}
		Integer min = Integer.parseInt(endDateMinutes.getText());
		if (min < 0 || min >= 60) {
			min = 0;
			endDateMinutes.setText("00");
		}
		date.setHours(hours);
		date.setMinutes(min);
		
		return date;
	}

	@Override
	public void setVisibleReadOnly(boolean v) {
		// TODO todavia no puse la visibilidad en la pantalla
		
	}

	@Override
	public void clear() {
		clearStart();
		clearEnd();
		clearCourse();
		clearType();
		clearArea();
		clearPersons();
		clearCharacteristics();
	}
	
	private void clearCharacteristics() {
		appointmentCharacteristics.clear();
		multimedia.setValue(false);
		capacity.setText("0");
	}
	
	private void clearCourse() {
		course.setAcceptableValues(new ArrayList<Course>());
	}
	
	private void clearType() {
		type.setAcceptableValues(new ArrayList<ReserveAttemptDateType>());		
	}
	
	private void clearArea() {
		area.setAcceptableValues(new ArrayList<Area>());
	}
	

	
	private void clearStart() {
		date.setValue(new Date());
		startDateHours.setText("00");
		startDateMinutes.setText("00");
	}
	
	private void clearEnd() {
		endDateHours.setText("00");
		endDateMinutes.setText("00");
	}
		
	
	@UiHandler("cancel")
	public void onCancel(ClickEvent event) {
		presenter.cancel();
	}
	
	@UiHandler("commit")
	public void onCommit(ClickEvent event) {
		presenter.persist();
	}
	
	/**
	 * Segun el valor del parametro enabled, habilita o no el listBox
	 * @param listBox
	 * @param enabled
	 */
	public void setEnabled(ValueListBox<?> listBox, final boolean enabled) {
		if (enabled) {
			DOM.removeElementAttribute(listBox.getElement(), "disabled");
		} else {
			DOM.setElementAttribute(listBox.getElement(), "disabled", "disabled");
		}
	}
	
	
	
	
	
	
	
	
	
	///////////////// Persons //////////////////////////
	
	private final List<Person> originalPersonsAvailableToSelect = new ArrayList<Person>();
	private final List<Person> personsAvailableToSelect = new ArrayList<Person>();
	private final List<Person> selectedPersons = new ArrayList<Person>();
	
	@UiField(provided=true) ValueListBox<Person> selectedPerson;
	@UiField(provided=true) DataGrid<Person> persons;
	@UiField Button addPerson;
	@UiField Button clearPersons;
	
	@UiHandler("addPerson")
	public void onAddPerson(ClickEvent event) {
		Person person = selectedPerson.getValue();
		if (person == null) {
			return;
		}
		selectedPersons.add(person);
		updatePersonsViewData();
	}
	
	@UiHandler("clearPersons")
	public void onClearPersons(ClickEvent event) {
		personsAvailableToSelect.clear();
		personsAvailableToSelect.addAll(originalPersonsAvailableToSelect);
		selectedPersons.clear();
		updatePersonsViewData();
	}

	
	private void updatePersonsViewData() {
		removeFromAvailablePersons(selectedPersons);
		updatePersonsList(selectedPersons);
	}
	
	
	private void updatePersonsList(List<Person> lpersons) {
		persons.setRowCount(lpersons.size());
		persons.setRowData(lpersons);
	}
	
	private void removeFromAvailablePersons(List<Person> persons) {
		personsAvailableToSelect.removeAll(persons);
		updateSelectedPerson(personsAvailableToSelect);
	}
	
	private void updateSelectedPerson(List<Person> persons) {
		selectedPerson.setValue((persons.size() > 0) ? persons.get(0) : null);
		selectedPerson.setAcceptableValues(persons);
	}
	
	@Override
	public void setRelatedPersonsReadOnly(boolean v) {
		addPerson.setEnabled(!v);
	}
	
	private void clearPersons() {
		selectedPersons.clear();
		removeFromAvailablePersons(new ArrayList<Person>(personsAvailableToSelect));
		updatePersonsList(selectedPersons);		
		personsAvailableToSelect.clear();
	}	
	
	
	private String fullNameDni(Person person) {
		if (person == null) {
			return "";
		}
		String name = person.getName();
		String lastName = person.getLastName();
		String dni = person.getDni();
		return ((name != null) ? name : "") + " " + ((lastName != null) ? lastName : "") + " " + ((dni != null) ? dni : "");
	}
	
	
	private void createPersons() {
		selectedPerson = new ValueListBox<Person>(new Renderer<Person>() {
			@Override
			public String render(Person person) {
				return fullNameDni(person);
			}
			@Override
			public void render(Person person, Appendable appendable) throws IOException {
				appendable.append(render(person));
			}
		});
		
		
		TextColumn<Person> name = new TextColumn<Person>() {
			@Override
			public String getValue(Person object) {
				if (object == null) {
					return "";
				}
				return ((object.getName() != null) ? object.getName() : "");
			}
		};
		
		TextColumn<Person> lastName = new TextColumn<Person>() {
			@Override
			public String getValue(Person object) {
				if (object == null) {
					return "";
				}
				return ((object.getLastName() != null) ? object.getLastName() : "");
			}
		};
		
		TextColumn<Person> dni = new TextColumn<Person>() {
			@Override
			public String getValue(Person object) {
				if (object == null) {
					return "";
				}
				return ((object.getDni() != null) ? object.getDni() : "");
			}
		};		
		
		persons = new DataGrid<Person>();
		persons.addColumn(name,"Nombre");
		persons.addColumn(lastName,"Apellido");
		persons.addColumn(dni,"Dni");
	}
	
	@Override
	public void setPersons(List<Person> persons) {
		originalPersonsAvailableToSelect.clear();
		personsAvailableToSelect.clear();
		if (persons != null) {
			originalPersonsAvailableToSelect.addAll(persons);
			personsAvailableToSelect.addAll(persons);
		}
		updatePersonsViewData();
	}

	@Override
	public void setRelatedPersons(List<Person> persons) {
		selectedPersons.clear();
		if (persons != null) {
			selectedPersons.addAll(persons);
		}
		updatePersonsViewData();
	}

	@Override
	public List<Person> getRelatedPersons() {
		return (new ArrayList<Person>(selectedPersons));
	}	
	
	
	
	//////////////////////////////////////////7
	
	
	
	
	
	
	
	
	

}
