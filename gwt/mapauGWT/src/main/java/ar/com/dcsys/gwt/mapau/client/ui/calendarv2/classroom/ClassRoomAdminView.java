package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.classroom;

import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;

public interface ClassRoomAdminView extends IsWidget {

	public interface Presenter {
		public void persistClassRoom(); //actualizar datos
		public void removeClassRoom(ClassRoom classRoom);
		public void persistCharacteristicQuantity();
		public void removeCharacteristicQuantity(CharacteristicQuantity classRoom);
	}

	public void clear(); //inicializar vista (inicializar widgets)
	public void setPresenter(Presenter presenter); //asignar presenter

	public void setClassRooms(List<ClassRoom> classRooms);
	public void setClassRoomsSelectionModel(SelectionModel<ClassRoom> selectionModel);
	public String getName();
	public void setName(String name);
	
	public void setCharacteristicQuantitys(List<CharacteristicQuantity> characteristicQuantitys);
	public void setCharacteristicQuantitySelectionModel(SelectionModel<CharacteristicQuantity> selectionModel);
	public void setQuantity(Long Quantity);
	public Long getQuantity();
	public void setCharacteristic(Characteristic characteristic);
	public Characteristic getCharacteristic();
	
	public void setCharacteristics(List<Characteristic> characteristics);
	
	public void setCharacteristicQuantityFormVisible(boolean visible);
}