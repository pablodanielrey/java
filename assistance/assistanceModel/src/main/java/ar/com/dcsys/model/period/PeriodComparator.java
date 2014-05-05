package ar.com.dcsys.model.period;

import java.util.Comparator;

public class PeriodComparator implements Comparator<Period> {

	@Override
	public int compare(Period p0, Period p1) {
		if (p0 == null && p1 == null) {
			return 0;
		}
		
		if (p0.getStart() == null && p1.getStart() == null) {
			return 0;
		}
		
		if (p0.getStart() == null) {
			return -1;
		}
		
		if (p1.getStart() == null) {
			return 1;
		}
		
		return (p0.getStart().compareTo(p1.getStart()));
	}	
	
}
