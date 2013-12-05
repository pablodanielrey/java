package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface ClassRoomsManager {

	public void findAllClassRooms(Receiver<List<ClassRoom>> classRooms);
	
}
