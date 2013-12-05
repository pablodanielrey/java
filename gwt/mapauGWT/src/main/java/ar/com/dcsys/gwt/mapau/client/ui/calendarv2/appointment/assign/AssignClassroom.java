package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.assign;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.AssignClassroomView;
import ar.com.dcsys.utils.ClassRoomSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

public class AssignClassroom extends Composite implements AssignClassroomView {

	private static AssignClassroomUiBinder uiBinder = GWT.create(AssignClassroomUiBinder.class);

	interface AssignClassroomUiBinder extends UiBinder<Widget, AssignClassroom> {
	}
	
	
	public AssignClassroom() {
		createUserValidationMessage();
		createActions();
		createClassroom();
		createRepetitions();
		initWidget(uiBinder.createAndBindUi(this));
		checkCapacity.addClickHandler(checkCapacityHandler);
	}
	
	
/* ---------------------------------------------------------------------------------------------- *
 * --------------------------------- CLASSROOM -------------------------------------------------- *
 * ---------------------------------------------------------------------------------------------- */
	
	private Long getCapacity(ClassRoom classRoom){
		List<CharacteristicQuantity> characteristicQuantitys = classRoom.getCharacteristicQuantity();
		
		if (characteristicQuantitys == null) {
			return null;
		}
		
		for (CharacteristicQuantity characteristicQuantity : characteristicQuantitys) {
			Characteristic characteristic = characteristicQuantity.getCharacteristic();
								
			if (characteristic.getName() == null) {
				continue;
			}
			
			if (characteristic.getName().equals("Capacidad")) {
				if (characteristicQuantity.getQuantity() != null) {
					return characteristicQuantity.getQuantity();
				}
			}			
		}
		
		return null;
	}
	
	
	@UiField(provided=true) DataGrid<ClassRoom> classroom;
	private ListDataProvider<ClassRoom> classroomDataProvider;
	private MultiSelectionModel<ClassRoom> classroomsSelections;
	
	private void createClassroom() {
		classroom = new DataGrid<ClassRoom>();
		
		TextColumn<ClassRoom> nameColumn = new TextColumn<ClassRoom>() {

			@Override
			public String getValue(ClassRoom cr) {
				
				if (cr == null || cr.getName() == null) {
					return "Nulo";
				}
				
				return cr.getName();
			}
		};
		
		TextColumn<ClassRoom> capacityColumn = new TextColumn<ClassRoom>()  {

			@Override
			public String getValue(ClassRoom classroom) {
				List<CharacteristicQuantity> characteristicQuantitys = classroom.getCharacteristicQuantity();
				
				if (characteristicQuantitys == null) {
					return "-";
				}
				
				for (CharacteristicQuantity characteristicQuantity : characteristicQuantitys) {
					Characteristic characteristic = characteristicQuantity.getCharacteristic();
										
					if (characteristic.getName() == null) {
						continue;
					}
					
					if (characteristic.getName().equals("Capacidad")) {
						if (characteristicQuantity.getQuantity() != null) {
							return String.valueOf(characteristicQuantity.getQuantity());
						}
					}			
				}
				
				return "-";
				
			}
			
		};
		
		classroom.addColumn(nameColumn, "Nombre");
		classroom.addColumn(capacityColumn, "Capacidad");
		
		
		
		//***** ordenamiento *****
		classroomDataProvider = new ListDataProvider<ClassRoom>();
		classroomDataProvider.addDataDisplay(classroom);
		
		
		ListHandler<ClassRoom> sortHandler = new ListHandler<ClassRoom>(classroomDataProvider.getList());
		classroom.addColumnSortHandler(sortHandler);
			
		//indicar de las columnas de classrooms, cuales seran sorteables
		nameColumn.setSortable(true);
		capacityColumn.setSortable(true);

		//indicar cual sera el sort principal
		classroom.getColumnSortList().push(nameColumn);

		//definir lógica de sorts
		sortHandler.setComparator(nameColumn, ClassRoomSort.getComparator());
	
		sortHandler.setComparator(capacityColumn, new Comparator<ClassRoom>() {
			
			@Override
			public int compare(ClassRoom classRoom1, ClassRoom classRoom2) {
				if (classRoom1 == null && classRoom2 == null) {
					return 0;
				}
				if (classRoom1 == null) {
					return -1;
				}
				if (classRoom2 == null) {
					return 1;
				}
				
				Long capacity1 = getCapacity(classRoom1);
				Long capacity2 = getCapacity(classRoom2);

				if (capacity1 == null && capacity2 == null) {
					return 0;
				}
					
				if (capacity1 == null) {
					return -1;
				}
				
				if (capacity2 == null) {
					return 1;
				}
				
				int comparation = capacity1.compareTo(capacity2);
				
				if (comparation != 0) return comparation;
				
				Comparator<ClassRoom> comparator = ClassRoomSort.getComparator();
				
				return comparator.compare(classRoom1, classRoom2);

			}

		});
		
		
		
		
	}
	
	@Override
	public void setClassRoomsSelectionModel(MultiSelectionModel<ClassRoom> selection) {
		this.classroomsSelections = selection;
		classroom.setSelectionModel(selection);
	}

	@Override
	public void setClassRooms(List<ClassRoom> classRooms) {
		clearClassRooms();
		
		if (classRooms == null) {
			return;
		}
		
		this.classroom.setVisibleRange(0, classRooms.size());
		classroomDataProvider.getList().clear();
		classroomDataProvider.getList().addAll(classRooms);
		classroomDataProvider.refresh();
		
		
		this.classroom.setRowData(classRooms);
		this.classroom.setRowCount(classRooms.size());
		
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ColumnSortEvent.fire( AssignClassroom.this.classroom, AssignClassroom.this.classroom.getColumnSortList());
			}
		});		
	}
	
	private void clearClassRooms() {
		classroom.setRowData(new ArrayList<ClassRoom>());
		classroom.setRowCount(0);
	}
		

/* ---------------------------------------------------------------------------------------------- *
 * --------------------------------- REPETITIONS ------------------------------------------------ *
 * ---------------------------------------------------------------------------------------------- */
	
	@UiField(provided=true) Label repetitionDescription;
	@UiField(provided=true) CheckBox sameHours;
	@UiField(provided=true) Label generateRepetition;
	
	private void createRepetitions() {
		
		generateRepetition = new Label();
		generateRepetition.setText("Seleccionar Reservas Relacionadas");
		generateRepetition.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				
				presenter.generateRepetition();
			}
		});
		
		repetitionDescription = new Label();
		repetitionDescription.setText("");
		
		sameHours = new CheckBox("Mismo Horario");
		sameHours.setValue(true);
		
	}
	
	@Override
	public boolean getSameHours() {
		return sameHours.getValue();
	}

	@Override
	public void setRepetitionSummary(String summary) {
		repetitionDescription.setText(summary);
	}
	
	private void clearRepetitions() {
		repetitionDescription.setText("");
		sameHours.setValue(true);
	}
	
	

	
/* ---------------------------------------------------------------------------------------------- *
 * ----------------------------------- SELECCION DE AULAS A MOSTRAR ----------------------------- *
 * ---------------------------------------------------------------------------------------------- */
	@UiField CheckBox checkCapacity;
	
	@Override
	public boolean getCheckCapacity() {
		return !checkCapacity.getValue();
	}
	
	@Override
	public void setCheckCapacity(boolean checkCapacity) {
		this.checkCapacity.setValue(checkCapacity);
	}

	/**
	 * clear fieldset showClassroom
	 */
	private void clearShowClassroom(){
		this.checkCapacity.setValue(false);
	}
	
	private final ClickHandler checkCapacityHandler = new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			if (presenter == null) {
				return;
			}
			
			presenter.findClassroom();
			
		}
		
	};
	
	
	
	
	
	
/* ---------------------------------------------------------------------------------------------- *
 * ----------------------------------- GENERALS ------------------------------------------------- *
 * ---------------------------------------------------------------------------------------------- */
	
	private AssignClassroomView.Presenter presenter;
	
	@UiField AssignClassroomViewResources res;
	
	@UiField(provided=true) Label cancel;
	@UiField(provided=true) Label commit;
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@Override
	public void clear() {
		clearClassRooms();
		clearRepetitions();
		clearShowClassroom();
		hideValidationMessage();
	}
	
	private void createActions() {
		cancel = new Label();
		cancel.setText("Cancelar");
		cancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				presenter.cancel();
			}
		});
		
		commit = new Label();
		commit.setText("Aceptar");
		commit.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				presenter.commit();
			}
		});
	}

/* ---------------------------------------------------------------------------------------------- *
 * -------------------------- USER VALIDATION MESSAGE ------------------------------------------- *
 * ---------------------------------------------------------------------------------------------- */	
	
	PopupPanel userValidationMessageContainer;

	private void createUserValidationMessage() {
		userValidationMessageContainer = new PopupPanel();
		userValidationMessageContainer.setGlassEnabled(true);
		userValidationMessageContainer.setModal(true);
		userValidationMessageContainer.setAnimationEnabled(true);
	}
	
	@Override
	public void showValidationMessage() {
		userValidationMessageContainer.center();
		userValidationMessageContainer.show();
	}


	@Override
	public void hideValidationMessage() {
		userValidationMessageContainer.hide();
	}


	@Override
	public AcceptsOneWidget getUserValidationContainer() {
		return userValidationMessageContainer;
	}

}
