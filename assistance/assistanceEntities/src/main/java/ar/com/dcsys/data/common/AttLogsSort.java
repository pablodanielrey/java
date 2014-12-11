package ar.com.dcsys.data.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;

public class AttLogsSort {
	
	private static final Comparator<AttLog> comparator = new Comparator<AttLog>() {
		@Override
		public int compare(AttLog arg0, AttLog arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}
			
			if (arg0.getDate() == null && arg1.getDate() == null) {
				return 0;
			}
			
			if (arg0.getDate() == null) {
				return -1;
			}
			
			if (arg1.getDate() == null) {
				return 1;
			}			
			return arg0.getDate().compareTo(arg1.getDate());
		}
	};
	
	public static void sort(List<AttLog> logs) {
		Collections.sort(logs, comparator);
	}

}
