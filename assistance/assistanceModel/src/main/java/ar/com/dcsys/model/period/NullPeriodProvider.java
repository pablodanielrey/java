package ar.com.dcsys.model.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.model.log.AttLogsManager;

/**
 * No genera ningún período. es por ejemplo cuando se da de baja a un usuario.
 * o deja de trabajar dentro de la organización.
 * 
 * @author pablo
 */
public class NullPeriodProvider implements PeriodProvider {

	@Override
	public boolean isUsable(PeriodType type) {
		return type.equals(PeriodType.NULL);
	}

	@Override
	public List<DefaultPeriodImpl> findPeriods(Date pstart, Date pend, Date start,	Date end, Person person, AttLogsManager logManager, boolean onlyWorkDays) throws PeriodException {
		return null;
	}

}
