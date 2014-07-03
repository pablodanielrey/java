package ar.com.dcsys.model.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.model.log.AttLogsManager;

public interface PeriodProvider {

	public boolean isUsable(PeriodType type);
	public List<Period> findPeriods(Date pstart, Date pend, Date start, Date end, Person person, AttLogsManager logManager, boolean onlyWorkDays) throws PeriodException;
	
}
