package ar.com.dcsys.gwt.assistance.shared;

import java.util.Date;

public class DailyPeriod {

	private Date start;
	private Date end;
	private Date date;
	private Integer minutes;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date d) {
		this.date = new Date(d.getTime());
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = new Date(start.getTime());
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = new Date(end.getTime());
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	
	
	
}
