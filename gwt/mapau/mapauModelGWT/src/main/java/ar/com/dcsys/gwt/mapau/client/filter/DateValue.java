package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.Date;

public class DateValue implements Comparable<DateValue> {
	private Date start;
	private Date end;
	
	public DateValue() {
		
	}
	
	public DateValue(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Override
	public int compareTo(DateValue o) {
		if (o == null || o.getStart() == null) {
			if (this.getStart() == null) {
				return 0;
			}
			return 1;
		}
		if (this.getStart() == null) {
			return -1;
		}
		if (!this.getStart().equals(o.getStart())) {
			return this.getStart().compareTo(o.getStart());
		}
		if (o.getEnd() == null) {
			if (this.getEnd() == null) {
				return 0;
			}
			return 1;
		}
		if (this.getEnd() == null) {
			return -1;
		} 		
		return this.getEnd().compareTo(o.getEnd());
	}
}
	
