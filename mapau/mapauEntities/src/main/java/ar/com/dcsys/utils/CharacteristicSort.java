package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;


public class CharacteristicSort {

	private static final Comparator<Characteristic> comparator = new Comparator<Characteristic>() {
		
		@Override
		public int compare(Characteristic arg0, Characteristic arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}
			
			
			String name0 = (arg0.getName() != null) ? arg0.getName() : "" ;
			if (name0 != null & name0.trim().equals("")) {
				name0 = null;
			}

			String name1 = (arg1.getName() != null) ? arg1.getName() : "" ;
			if (name1 != null & name1.trim().equals("")) {
				name1 = null;
			}
			
			if (name0 == null && name1 == null) {
				return 0 ;
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
	
	public static Comparator<Characteristic> getComparator() {
		return comparator;
	}
	
	public static void sort(List<Characteristic> list) {
		Collections.sort(list,getComparator());		
	}
	
}
