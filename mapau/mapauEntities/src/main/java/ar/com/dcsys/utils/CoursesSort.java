package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.silabouse.Course;

public class CoursesSort {
	private static final Comparator<Course> comparator = new Comparator<Course>() {
		
		@Override
		public int compare(Course arg0, Course arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}
			
			
			
			String name0 = (((arg0.getSubject() != null && arg0.getSubject().getName() != null) ? arg0.getSubject().getName() : "") + ((arg0.getName() != null) ? arg0.getName() : "")).trim().toLowerCase();
			if (name0 != null & name0.trim().equals("")) {
				name0 = null;
			}
			String name1 = (((arg1.getSubject() != null && arg1.getSubject().getName() != null) ? arg1.getSubject().getName() : "") + ((arg1.getName() != null) ? arg1.getName() : "")).trim().toLowerCase();
			if (name1 != null & name1.trim().equals("")) {
				name1 = null;
			}

						
			if (name0 == null) {
				return -1;
			}
			
			if (name1 == null) {
				return 1;
			}
			
			return name0.compareTo(name1);
		}
	};
	
	public static Comparator<Course> getComparator() {
		return comparator;
	}
	
	public static void sort(List<Course> courses) {
		Collections.sort(courses,getComparator());		
	}
	
}
