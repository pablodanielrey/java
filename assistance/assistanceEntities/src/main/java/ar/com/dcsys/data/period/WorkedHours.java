package ar.com.dcsys.data.period;

import java.util.List;

import ar.com.dcsys.data.log.AttLog;

public interface WorkedHours {

	public AttLog getInLog();
	public AttLog getOutLog();
	public List<AttLog> getLogs();
	public Long getWorkedMilis();
		
	
}
