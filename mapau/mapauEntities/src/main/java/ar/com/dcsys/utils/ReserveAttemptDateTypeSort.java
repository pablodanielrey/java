package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDateType;

public class ReserveAttemptDateTypeSort {
	private static final Comparator<ReserveAttemptDateType> comparator = new Comparator<ReserveAttemptDateType>() {
		
		@Override
		public int compare(ReserveAttemptDateType arg0, ReserveAttemptDateType arg1) {
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
	
	public static Comparator<ReserveAttemptDateType> getComparator() {
		return comparator;
	}
	
	public static void sort(List<ReserveAttemptDateType> reserveAttemptDateTypes) {
		Collections.sort(reserveAttemptDateTypes,getComparator());		
	}
	
}
