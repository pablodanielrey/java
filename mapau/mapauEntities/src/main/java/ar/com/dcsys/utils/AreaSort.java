package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.silabouse.Area;

public class AreaSort {
	private static final Comparator<Area> comparator = new Comparator<Area>() {
		
		@Override
		public int compare(Area arg0, Area arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}

			String name0 = arg0.getName().toLowerCase();
			if (name0 != null & name0.trim().equals("")) {
				name0 = null;
			}
			String name1 = arg1.getName().toLowerCase();
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
	
	public static Comparator<Area> getComparator() {
		return comparator;
	}
	
	public static void sort(List<Area> areas) {
		Collections.sort(areas,getComparator());		
	}
	
}
