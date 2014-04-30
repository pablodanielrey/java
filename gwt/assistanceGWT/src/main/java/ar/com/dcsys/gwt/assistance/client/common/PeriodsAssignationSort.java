package ar.com.dcsys.gwt.assistance.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.period.PeriodAssignation;

public class PeriodsAssignationSort {

	private static final Comparator<PeriodAssignation> comparator = new Comparator<PeriodAssignation>() {
		@Override
		public int compare(PeriodAssignation arg0, PeriodAssignation arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}

			int valueReturn = arg0.getStart().compareTo(arg1.getStart());
			if (valueReturn == 0) {
				valueReturn = arg0.getType().compareTo(arg1.getType());
			}
			return valueReturn;
		}
	};
	
	public static void sort(List<PeriodAssignation> periods) {
		Collections.sort(periods, comparator);
	}

}
