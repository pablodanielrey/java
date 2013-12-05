package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.classroom;

import ar.com.dcsys.data.classroom.ClassRoom;

public class ClassRoomNameFilter implements ClassRoomFilter{

	@Override
	public boolean checkFilter(ClassRoom classRoom, String filter) {
		if (classRoom.getName() == null) {
			return false;
		} else {
			return classRoom.getName().toLowerCase().contains(filter.toLowerCase());
		}
	}

}