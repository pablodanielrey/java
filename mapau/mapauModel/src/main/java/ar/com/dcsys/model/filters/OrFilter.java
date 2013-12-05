package ar.com.dcsys.model.filters;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;

public class OrFilter implements ReserveAttemptDateFilter {

	private final ReserveAttemptDateFilter right;
	private final ReserveAttemptDateFilter left; 
	
	public OrFilter(ReserveAttemptDateFilter right, ReserveAttemptDateFilter left) {
		this.right = right;
		this.left = left;
	}	
	
	@Override
	public boolean filter(ReserveAttemptDate ra) throws FilterException {
		return (left.filter(ra) || right.filter(ra));
	}

	@Override
	public Class getType() {
		return this.getClass();
	}

}
