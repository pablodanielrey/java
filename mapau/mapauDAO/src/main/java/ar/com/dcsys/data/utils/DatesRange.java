package ar.com.dcsys.data.utils;

import java.util.Date;

public class DatesRange {

	public DatesRange(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	private final Date start;
	private final Date end;
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	
	
}
