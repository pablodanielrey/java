package ar.com.dcsys.gwt.assistance.client.ui.period;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.WorkedHours;

public class WorkedHoursUtil {
	
	public static Date getLastDate(List<? extends WorkedHours> hours) {
		Date last = null;
		
		//para que funcione bien deben estar ordenados correctamente
		Collections.sort(hours,new Comparator<WorkedHours>() {
			@Override
			public int compare(WorkedHours arg0, WorkedHours arg1) {
				
				if (arg0 == null && arg1 == null) {
					return 0;
				}
				
				if (arg0 == null) {
					return -1;
				}
				
				if (arg1 == null) {
					return 1;
				}
				
				return arg0.getInLog().getDate().compareTo(arg1.getInLog().getDate());
			}
		});
		
		for (WorkedHours wh : hours) {
			if (wh.getOutLog() != null) {
				last = wh.getOutLog().getDate();
			}
		}
		return last;
	}
	
	public static int getWorkedHours(List<? extends WorkedHours> hours) {
		return getWorkedHours(getWorkedMilis(hours));
	}
	
	public static int getWorkedMinutes(List<? extends WorkedHours> hours) {
		return getWorkedMinutes(getWorkedMilis(hours));
	}
	
	public static int getWorkedMinutes(long workedMilis) {
		int workedTotalMinutes = (int)(workedMilis / (1000 * 60)); 
		return (workedTotalMinutes % 60);
	}
	
	public static int getWorkedHours(long workedMilis) {
		int workedTotalMinutes = (int)(workedMilis / (1000 * 60)); 
		return (workedTotalMinutes / 60);
	}
	
	private static long getWorkedMilis(List<? extends WorkedHours> hours) {
		if (hours == null) {
			return 0;
		}

		long milis = 0;
		for (WorkedHours wh : hours) {
			if (wh.getInLog() != null && wh.getOutLog() != null) {
				milis += wh.getWorkedMilis();
			}
		}
		return milis;
	}
	
	/**
	 * En el caso de ser necesario agrega un 0 adelante del numero. para que quede en formato de 2 digitos.
	 * @param n
	 * @return
	 */
	static public String fhm(int n) {
		String number = String.valueOf(n);
		return (number.length() < 2) ? "0" + number : number;
	}		
}
