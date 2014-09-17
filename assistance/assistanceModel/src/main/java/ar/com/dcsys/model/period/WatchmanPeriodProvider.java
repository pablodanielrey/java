package ar.com.dcsys.model.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeWatchman;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.model.log.AttLogsManager;

/**
 * No genera ningún período. es por ejemplo cuando se da de baja a un usuario.
 * o deja de trabajar dentro de la organización.
 * 
 * @author pablo
 */
public class WatchmanPeriodProvider implements PeriodProvider {

	@Override
	public boolean isUsable(PeriodType type) {
		return type instanceof PeriodTypeWatchman;
	}

	@Override
	public List<Period> findPeriods(Date pstart, Date pend, Date start,	Date end, Person person, AttLogsManager logManager, boolean onlyWorkDays, PeriodType type) throws PeriodException {
		return null;
	}

}
