package ar.com.dcsys.model.period;

import java.util.Collections;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.period.WorkedHours;

public class DefaultWorkedHoursImpl implements WorkedHours {

	private AttLog inLog;
	private AttLog outLog;
	private List<AttLog> logs;
	private long milis;
	
	// constructor para los proxys y request factory. no porque lo use en el cógio.
	public DefaultWorkedHoursImpl() {
		
	}
	
	public DefaultWorkedHoursImpl(AttLog inLog, AttLog outLog, List<AttLog> logs) {
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

	@Override
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

	@Override
	public AttLog getOutLog() {
		return outLog;
	}

	@Override
	public List<AttLog> getLogs() {
		return logs;
	}	
	
	@Override
	public Long getWorkedMilis() {
		return milis;
	}
	
	public void setWorkedMilis(Long m) {
		milis = m;
	}
	
}
