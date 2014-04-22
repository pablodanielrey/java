package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.classroom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;

public class ClassRoomAdmin extends Composite implements ClassRoomAdminView {

	private static ClassRoomAdminUiBinder uiBinder = GWT
			.create(ClassRoomAdminUiBinder.class);

	interface ClassRoomAdminUiBinder extends UiBinder<Widget, ClassRoomAdmin> {
	}
	
	private Presenter presenter ;

	private final ClassRoomFilter[] classRoomFilters = new ClassRoomFilter[] { new ClassRoomNameFilter() };
	private final List<ClassRoom> classRooms;
	private final List<ClassRoom> classRoomsFiltered;
	private Timer filterTimer = null;

	private final List<CharacteristicQuantity> characteristicQuantitys;

	@UiField(provided=true) TextBox classRoomGridFilter;
	@UiField(provided=true) Label classRoomGridCount;
	@UiField(provided=true) DataGrid<ClassRoom> classRoomGrid;
	@UiField TextBox name;
	@UiField FlowPanel characteristicQuantityListForm;

	
	@UiField(provided = true) DataGrid<CharacteristicQuantity> characteristicQuantityGrid;
	@UiField TextBox quantity;
	@UiField(provided = true) ValueListBox<Characteristic> characteristicListBox;
	
	
	public ClassRoomAdmin() {
		createClassRoomGrid(); 
		createClassRoomGridFilter();
		createCharacteristicQuantityGrid();
		createCharacteristicListBox();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		classRooms = new ArrayList<ClassRoom>();
		characteristicQuantitys = new ArrayList<CharacteristicQuantity>();
		classRoomsFiltered = new ArrayList<ClassRoom>();
	}
	
	/**
	 * inicializar elementos asociados a classRoom
	 */
	private void clearClassRoomData(){
		name.setText("");
		
		classRoomGridFilter.setText("");
		classRoomGridCount.setText("0");
		classRoomGrid.setRowCount(0, true);
		classRoomGrid.setRowData(new ArrayList<ClassRoom>());
		
	}
	
	/**
	 * inicializar elementos asociados a characteristicQuantity
	 */
	private void clearCharacteristicQuantityData(){
		quantity.setText("0");
		characteristicListBox.setValue(null);
			
		characteristicQuantityGrid.setRowCount(0, true);
		characteristicQuantityGrid.setRowData(new ArrayList<CharacteristicQuantity>());
	}
	
	/**
	 * inicializar elementos asociados a characteristic
	 */
	private void clearCharacteristicData(){
		characteristicListBox.setValue(null);
		characteristicListBox.setAcceptableValues(new ArrayList<Characteristic>());

	}

	private void createClassRoomGrid() {
		classRoomGrid = new DataGrid<ClassRoom>();
		classRoomGridCount = new Label("0");

		//columna 1: descripcion
		TextColumn<ClassRoom> name = new TextColumn<ClassRoom>() {
		      @Override
		      public String getValue(ClassRoom classRoom) {
		        return classRoom.getName();
		      }
		};
		classRoomGrid.addColumn(name);

		//columna 2: boton eliminar
		ActionCell<ClassRoom> deleteCell = new ActionCell<ClassRoom>("Eliminar",new Delegate<ClassRoom>() {
			@Override
			public void execute(ClassRoom classRoom) {
				if (presenter == null) {
					return;
				}
				presenter.removeClassRoom(classRoom);
			}
		});
		IdentityColumn<ClassRoom> delete = new IdentityColumn<ClassRoom>(deleteCell);
		classRoomGrid.addColumn(delete);
		
	}
	
	private void createClassRoomGridFilter() {
			
		classRoomGridFilter = new TextBox();
		
		classRoomGridFilter.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimer != null) {
					filterTimer.cancel();
				}
				filterTimer = new Timer() {
					public void run() {
						filterTimer = null;
						executeClassRoomGridFilter();							
					};
				};
				filterTimer.schedule(2000);				
			}
		});
	}
	
	private void executeClassRoomGridFilter() {
		String classRoomGridFilterText = classRoomGridFilter.getText();
		if (classRoomGridFilterText == null || classRoomGridFilterText.trim().equals("")) {	
			classRoomGrid.setRowCount(this.classRooms.size());
			classRoomGrid.setRowData(this.classRooms);
			classRoomGridCount.setText(String.valueOf(classRooms.size()));
			return;
		}
		
		classRoomGridFilterText = classRoomGridFilterText.toLowerCase();
		classRoomsFiltered.clear();
		for (ClassRoom classRoom : classRooms) {
			for (ClassRoomFilter classRoomFilter : classRoomFilters) {
				if (classRoomFilter.checkFilter(classRoom, classRoomGridFilterText)) {
					classRoomsFiltered.add(classRoom);
					break;
				}
			}
		}
		classRoomGrid.setRowCount(this.classRoomsFiltered.size());
		classRoomGrid.setRowData(this.classRoomsFiltered);
		classRoomGridCount.setText(String.valueOf(classRoomsFiltered.size()));
	}
	
	
	
	
	
	private void createCharacteristicQuantityGrid(){
		characteristicQuantityGrid = new DataGrid<CharacteristicQuantity>();		

		//columna: id
		TextColumn<CharacteristicQuantity> characteristic = new TextColumn<CharacteristicQuantity>() {
		      @Override
		      public String getValue(CharacteristicQuantity characteristicQuantity) {
		        return characteristicQuantity.getCharacteristic().getName();
		      }
		};
		characteristicQuantityGrid.addColumn(characteristic);
		
		//columna: quantity
		TextColumn<CharacteristicQuantity> quantity = new TextColumn<CharacteristicQuantity>() {
		      @Override
		      public String getValue(CharacteristicQuantity characteristicQuantity) {
		        return String.valueOf(characteristicQuantity.getQuantity());
		      }
		};
		characteristicQuantityGrid.addColumn(quantity);

	
		//columna 2: boton eliminar
		ActionCell<CharacteristicQuantity> deleteCell = new ActionCell<CharacteristicQuantity>("Eliminar",new Delegate<CharacteristicQuantity>() {
			@Override
			public void execute(CharacteristicQuantity characteristicQuantity) {
				if (presenter == null) {
					return;
				}
				presenter.removeCharacteristicQuantity(characteristicQuantity);
			}
		});
		IdentityColumn<CharacteristicQuantity> delete = new IdentityColumn<CharacteristicQuantity>(deleteCell);
		characteristicQuantityGrid.addColumn(delete);
	}
	
	private void createCharacteristicListBox() {
		characteristicListBox = new ValueListBox<Characteristic>(new Renderer<Characteristic>() {
			@Override
			public String render(Characteristic characteristic) {
				if (characteristic == null) {
					return "";
				}
				return characteristic.getName();
			}

			@Override
			public void render(Characteristic characteristic, Appendable appendable) throws IOException {
				if (characteristic == null) {
					return;
				}
				appendable.append(characteristic.getName());
			}
		});
	}

		
	
	
	@Override
	/**
	 * inicializar todo
	 */
	public void clear() {
		clearCharacteristicData();
		clearClassRoomData();
		clearCharacteristicQuantityData();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setClassRooms(List<ClassRoom> classRooms) {
		this.classRooms.clear();
		this.classRooms.addAll(classRooms);
		classRoomGrid.setRowCount(this.classRooms.size(), true);
		classRoomGrid.setRowData(this.classRooms);
		classRoomGridCount.setText(String.valueOf(classRooms.size()));
		executeClassRoomGridFilter();
	}
	
	@Override
	public String getName() {
		return name.getText();
	}
	
	@Override
	public void setName(String name) {
		this.name.setText(name);
	}
	
	@UiHandler("classRoomAccept")
	public void onClickClassRoomAccept(ClickEvent e) {
		presenter.persistClassRoom();
	}

	@Override
	public void setCharacteristicQuantitys(List<CharacteristicQuantity> characteristicQuantitys) {
		this.characteristicQuantitys.clear();
		this.characteristicQuantitys.addAll(characteristicQuantitys);
		characteristicQuantityGrid.setRowCount(this.characteristicQuantitys.size(), true);
		characteristicQuantityGrid.setRowData(this.characteristicQuantitys);
	}

	@Override
	public void setQuantity(Long quantity) {
		this.quantity.setText(String.valueOf(quantity));
	}

	@Override
	public Long getQuantity() {
		return Long.parseLong(quantity.getText());
	}

	@Override
	public void setCharacteristics(List<Characteristic> characteristics) {
		characteristicListBox.setAcceptableValues(characteristics);
		characteristicListBox.setValue(characteristics.get(0));
	}

	@Override
	public void setCharacteristic(Characteristic characteristic) {
		characteristicListBox.setValue(characteristic);
	}

	@Override
	public Characteristic getCharacteristic() {
		return characteristicListBox.getValue();
	}
	
	@UiHandler("characteristicQuantityAdd")
	public void onClickCharacteristicQuantityAdd(ClickEvent e) {
		presenter.persistCharacteristicQuantity();
	}

	@Override
	public void setClassRoomsSelectionModel(SelectionModel<ClassRoom> selectionModel) {
		if (selectionModel == null){
			return;
		}

		classRoomGrid.setSelectionModel(selectionModel);
		
	}

	@Override
	public void setCharacteristicQuantitySelectionModel(SelectionModel<CharacteristicQuantity> selectionModel) {
		if (selectionModel == null){
			return;
		}

		characteristicQuantityGrid.setSelectionModel(selectionModel);
	}

	@Override
	public void setCharacteristicQuantityFormVisible(boolean visible) {
		characteristicQuantityListForm.setVisible(visible);
		
	}

}
