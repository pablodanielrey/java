package ar.com.dcsys.data.period;

import java.util.Collections;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;

public class WorkedHours {

	private AttLog inLog;
	private AttLog outLog;
	private List<AttLog> logs;
	private long workedMilis;
	
	// constructor para los proxys y request factory. no porque lo use en el cógio.
	public WorkedHours() {
		
	}
	
	public WorkedHours(AttLog inLog, AttLog outLog, List<AttLog> logs) {
		this.inLog = inLog;
		this.outLog = outLog;
		this.logs = Collections.unmodifiableList(logs);
		this.workedMilis = calcMilis();
	}
	
	private final long calcMilis() {
		if (inLog == null) {
			return 0;
		}
		
		if (outLog == null) {
			return 0;
		}

		long in = inLog.getDate().getTime();
		long out = outLog.getDate().getTime();
		
		return Math.max(in, out) - Math.min(in, out);
	}

	public AttLog getInLog() {
		return inLog;
	}

	public void setInLog(AttLog inLog) {
		this.inLog = inLog;
	}

	public void setOutLog(AttLog outLog) {
		this.outLog = outLog;
	}

	public void setLogs(List<AttLog> logs) {
		this.logs = logs;
	}

	public AttLog getOutLog() {
		return outLog;
	}

	public List<AttLog> getLogs() {
		return logs;
	}

	public long getWorkedMilis() {
		return workedMilis;
	}

	public void setWorkedMilis(long workedMilis) {
		this.workedMilis = workedMilis;
	}	
	
	
	
}
