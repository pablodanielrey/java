package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.classroom;

import ar.com.dcsys.data.classroom.ClassRoom;

public interface ClassRoomFilter {
	public boolean checkFilter(ClassRoom classRoom, String filter);
}