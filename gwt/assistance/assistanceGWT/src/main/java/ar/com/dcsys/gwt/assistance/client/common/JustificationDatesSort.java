package ar.com.dcsys.gwt.assistance.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;

public class JustificationDatesSort {
	private static final Comparator<JustificationDate> comparator = new Comparator<JustificationDate>() {
	
		private Integer verifyNull (Object arg0, Object arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}			
			return null;
		}
		
		private int comparePerson(Person p1, Person p2) {
			Integer valueNull = verifyNull(p1,p2);
			if (valueNull != null) {
				return valueNull;
			}
			return (int)(Long.parseLong(p1.getDni()) - Long.parseLong(p2.getDni()));
		}
		
		private int compareDate (Date arg0, Date arg1) {
			Integer valueNull = verifyNull(arg0,arg1);
			if (valueNull != null) {
				return valueNull;
			}			
			return arg0.compareTo(arg1);
		}
		
		private int compareJustification(Justification arg0, Justification arg1) {
			Integer valueNull = verifyNull(arg0,arg1);
			if (valueNull != null) {
				return valueNull;
			}	
			return arg0.getCode().trim().compareTo(arg1.getCode().trim()); 
		}
		
		@Override
		public int compare(JustificationDate arg0, JustificationDate arg1) {
			Integer valueNull = verifyNull(arg0,arg1);
			if (valueNull != null) {
				return valueNull;
			}	
			
			int valueReturn = comparePerson(arg0.getPerson(),arg1.getPerson());
			if (valueReturn != 0) {
				return valueReturn;
			}
			valueReturn = compareDate(arg0.getStart(), arg1.getStart());
			if (valueReturn != 0) {
				return valueReturn;
			}
			valueReturn = compareDate(arg0.getEnd(), arg1.getEnd());
			if (valueReturn != 0) {
				return valueReturn;
			}
			return compareJustification(arg0.getJustification(), arg1.getJustification());
		}
	};
	
	public static void sort(List<JustificationDate> justifications) {
		Collections.sort(justifications, comparator);
	}
}
