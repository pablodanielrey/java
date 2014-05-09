package ar.com.dcsys.model.period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.model.log.AttLogsManager;


public class SystemPeriodProvider implements PeriodProvider {

	@Override
	public boolean isUsable(PeriodType type) {
		return type.equals(PeriodType.SYSTEMS);
	}

	@Override
	public List<DefaultPeriodImpl> findPeriods(Date pstart, Date pend, Date start,	Date end, Person person, AttLogsManager logManager, boolean onlyWorkDays) throws PeriodException {

		try {
			
			// calculo las fechas efectivas para las consultas.
			Date estart = pstart.before(start) ? start : pstart;
			Date eend = pend.before(end) ? pend : end;
			
			List<AttLog> logs = logManager.findAll(person, estart, eend);
			
			// ordeno los logs desde el mas temprano al mas tarde.
			Collections.sort(logs,new Comparator<AttLog>() {
				@Override
				public int compare(AttLog arg0, AttLog arg1) {
					if (arg0 == null && arg1 == null) {
						return 0;
					}
					if (arg0 == null) {
						return -1;
					}
					if (arg1 == null) {
						return 1;
					}
					return arg0.getDate().compareTo(arg1.getDate());
				}
			});

			List<List<AttLog>> logsPerDay = organize(logs);
			
			List<DefaultPeriodImpl> periods = new ArrayList<DefaultPeriodImpl>();

			long TOLERANCIA = 1000l * 60l * 15l;
			
			for (List<AttLog> ls : logsPerDay) {
				
				DefaultPeriodImpl p = new DefaultPeriodImpl();
				
				List<DefaultWorkedHoursImpl> whs = new ArrayList<DefaultWorkedHoursImpl>();
				if (ls.size() == 1) {
					whs.add(new DefaultWorkedHoursImpl(ls.get(0),null,ls));
				} else if (ls.size() >= 2) {
					whs.add(new DefaultWorkedHoursImpl(ls.get(0),ls.get(ls.size() - 1),ls));
				}
				
				// ya seteo los parámetros de la fecha y las marcaciones.
				if (whs.size() > 0) {
					AttLog initialLog = whs.get(0).getInLog();
					if (initialLog != null) {
						p.setStart(initialInDay(initialLog.getDate()));
						p.setEnd(finalInDay(initialLog.getDate()));
					}
				}

				p.setPerson(person);
				p.setWorkedHours(whs);
				whs = null;
				
				periods.add(p);
			}
			
			
			// agrego las faltas.
			List<DefaultPeriodImpl> absent = new ArrayList<>();
			
			if (periods.size() <= 0) {
				
				absent.addAll(getAbsent(person, estart, eend));
				
			} else {
			
				long aDay = 1000l * 60l * 60l * 24l;
				
				// genero las faltas iniciales hasta el primer período
				DefaultPeriodImpl firstPeriod = periods.get(0);
				if (estart.before(firstPeriod.getStart())) {
					
					Date endAbsent = new Date(firstPeriod.getStart().getTime() - aDay);
					absent.addAll(getAbsent(person, estart, endAbsent));
					
				}
				
				// geneero las faltas intermedias entre los perídos
				long previousPeriod = firstPeriod.getStart().getTime();
				for (DefaultPeriodImpl p : periods) {
					long actualPeriod = p.getStart().getTime();
					
					if (previousPeriod == actualPeriod) {
						previousPeriod = actualPeriod;
						continue;
					}
					
					long daysElapsed = ((actualPeriod - previousPeriod) / aDay);
					if (daysElapsed > 1) {
						absent.addAll(getAbsent(person, new Date(previousPeriod + aDay), new Date(actualPeriod - aDay)));
					}
					
					previousPeriod = actualPeriod;
				}

				// genero las faltas finales despues del ultimo período
				long endQuery = initialInDay(eend).getTime();
				long daysElapsed = (endQuery - previousPeriod) / aDay;
				if (daysElapsed > 0) {
					absent.addAll(getAbsent(person, new Date(previousPeriod + aDay), new Date(endQuery)));
				}
			}
			
			periods.addAll(absent);
			return periods;
			
		} catch (Exception e) {
			throw new PeriodException(e);
		}
	}

	
	/**
	 * REtorna períodos que representan ausencias.
	 * las fechas son inclusivas!!!.
	 * o sea :
	 * 12/2/13 --- 13/2/13 = 12,13
	 * @param start
	 * @param end
	 * @return
	 */
	private List<DefaultPeriodImpl> getAbsent(Person person, Date start, Date end) {
		long aDay = 1000l * 60l * 60l * 24l;
		long actualDayStart = initialInDay(start).getTime();
		long queryEnd = initialInDay(end).getTime();
		List<DefaultPeriodImpl> absent = new ArrayList<DefaultPeriodImpl>();
		while (actualDayStart <= queryEnd) {
			// genero una falta
			DefaultPeriodImpl a = new DefaultPeriodImpl();
			a.setPerson(person);
			a.setStart(new Date(actualDayStart));
			a.setEnd(new Date(actualDayStart + aDay - 1l));
			absent.add(a);
			actualDayStart = actualDayStart + aDay;
		}
		return absent;
	}
	
	
	private List<List<AttLog>> organize(List<AttLog> logs) {

		Date dayEnd = null;
		
		List<List<AttLog>> returnData = new ArrayList<>();

		List<AttLog> dayLogs = null;
		for (AttLog l : logs) {
			Date d = l.getDate();
			
			if (dayEnd == null) {
				dayEnd = finalInDay(d);
				dayLogs = new ArrayList<>();
			}

			if (d.after(dayEnd)) {
				returnData.add(dayLogs);

				dayEnd = finalInDay(d);
				dayLogs = new ArrayList<>();
			}
			
			dayLogs.add(l);
		}
		
		if (dayLogs != null && dayLogs.size() > 0) {
			returnData.add(dayLogs);
		}
		
		return returnData;
	}
	
	
			
	private Date initialInDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTime();
	}

	private Date finalInDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.MILLISECOND,999);
		return c.getTime();
	}	
	
}
