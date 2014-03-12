package ar.com.dcsys.model.period;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;

public class WorkedHours implements Serializable {

	private static final long serialVersionUID = 1L;
	private AttLog inLog;
	private AttLog outLog;
	private List<AttLog> logs;
	private long milis;
	
	// constructor para los proxys y request factory. no porque lo use en el cógio.
	public WorkedHours() {
		
	}
	
	public WorkedHours(AttLog inLog, AttLog outLog, List<AttLog> logs) {
		this.inLog = inLog;
		this.outLog = outLog;
		this.logs = Collections.unmodifiableList(logs);
		this.milis = calcMilis();
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
	
	public Long getWorkedMilis() {
		return milis;
	}
	
	public void setWorkedMilis(Long m) {
		milis = m;
	}
	
}
