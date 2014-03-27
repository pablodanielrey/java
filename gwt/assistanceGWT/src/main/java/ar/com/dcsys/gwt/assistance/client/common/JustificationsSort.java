package ar.com.dcsys.gwt.assistance.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;

public class JustificationsSort {
	private static final Comparator<Justification> comparator = new Comparator<Justification>() {
		@Override
		public int compare(Justification arg0, Justification arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}

			return arg0.getCode().trim().compareTo(arg1.getCode().trim());
		}
	};
	
	public static void sort(List<Justification> justifications) {
		Collections.sort(justifications, comparator);
	}
}
