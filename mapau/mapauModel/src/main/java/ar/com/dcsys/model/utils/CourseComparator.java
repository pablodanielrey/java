package ar.com.dcsys.model.utils;

import java.util.Comparator;

import ar.com.dcsys.data.silabouse.Course;

/**
 * Implementa un comparator para los cursos.
 * Solo chequea el id.
 * @author pablo
 *
 */
public class CourseComparator implements Comparator<Course> {

	@Override
	public int compare(Course o1, Course o2) {
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
