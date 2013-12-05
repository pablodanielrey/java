package ar.com.dcsys.model.utils;

import java.util.Comparator;

import ar.com.dcsys.data.reserve.ReserveAttemptDateType;

/**
 * Implementa un comparator para los ReserveAttemptType
 * solo chequea los ids.
 * 
 * @author pablo
 *
 */
public class ReserveAttemptTypeComparator implements Comparator<ReserveAttemptDateType> {

	@Override
	public int compare(ReserveAttemptDateType o1, ReserveAttemptDateType o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		
		if (o1 == null) {
			return -1;
		}
		
		if (o2 == null) {
			return 1;
		}
		
		if (o1.getId() == null && o2.getId() == null) {
			return 0;
		}
		
		if (o1.getId() == null) {
			return -1;
		}
		
		if (o2.getId() == null) {
			return 1;
		}
		
		return o1.getId().compareTo(o2.getId());
	}
	
}
