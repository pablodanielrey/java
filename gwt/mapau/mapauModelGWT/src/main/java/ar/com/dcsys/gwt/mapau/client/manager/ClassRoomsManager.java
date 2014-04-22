package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface ClassRoomsManager {

	public void findAllClassRooms(Receiver<List<ClassRoom>> rec);
	public void findAllCharacteristics(Receiver<List<Characteristic>> rec);
	public void create(ClassRoom classRoom, CharacteristicQuantity c, Receiver<Void> rec);
	
}
