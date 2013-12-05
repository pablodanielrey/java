package ar.com.dcsys.model.filters.types;

import java.util.Comparator;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.model.filters.PropertyAccesor;
import ar.com.dcsys.model.filters.PropertyFilter;
import ar.com.dcsys.model.utils.ClassRoomComparator;

public class ClassRoomFilter extends PropertyFilter<ClassRoom> {

	private static final Comparator<ClassRoom> c = new ClassRoomComparator();
	
	public ClassRoomFilter(ClassRoom classRoom, PropertyAccesor<ClassRoom> pc) throws FilterException {
		super(classRoom, c, pc);
	}

}
