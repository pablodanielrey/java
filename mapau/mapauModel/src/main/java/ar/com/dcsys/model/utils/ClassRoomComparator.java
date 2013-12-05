package ar.com.dcsys.model.utils;

import java.util.Comparator;

import ar.com.dcsys.data.classroom.ClassRoom;

/**
 * Comparator entre ClassRooms
 * solo compara los ids de las classRooms.
 * 
 * @author pablo
 *
 */
public class ClassRoomComparator implements Comparator<ClassRoom> {

	@Override
	public int compare(ClassRoom o1, ClassRoom o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		
		if (o1 == null) {
			return -1;
		}
		
		if (o2 == null) {
			return 1;
		}
		
		if (o1.getId() == null && o2.getId() == null) {
			return 0;
		}
		
		if (o1.getId() == null) {
			return -1;
		}
		
		if (o2.getId() == null) {
			return 1;
		}
		
		return o1.getId().compareTo(o2.getId());
	}
	
}
