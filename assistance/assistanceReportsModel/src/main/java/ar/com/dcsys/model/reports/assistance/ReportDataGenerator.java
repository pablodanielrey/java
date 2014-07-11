package ar.com.dcsys.model.reports.assistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.justification.JustificationsManager;
import ar.com.dcsys.model.period.PeriodComparator;
import ar.com.dcsys.model.period.PeriodsManager;
import ar.com.dcsys.utils.PersonSort;

public class ReportDataGenerator {
	
	private static final Logger logger = Logger.getLogger(ReportDataGenerator.class.getName());

	@Inject PeriodsManager periodsManager;
	@Inject JustificationsManager justificationManager;
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	
	public ReportSummary getReport(Date start, Date end, List<Person> personsToReport) throws IOException {
		return getReport(start, end, personsToReport,false);
	}
		
	private void removeMilisDate(Date date) {
		long milis = date.getTime();
		date.setTime(date.getTime()-(milis % 1000));
	}
	
	public ReportSummary getReport(Date start, Date end, List<Person> personsToReport,Boolean onlyWorkDays) throws IOException {
	
		try {
		
			//elimino los milis segundo de la fecha End
			removeMilisDate(end);
			
			// busco las justificaciones generales.
			
			List<GeneralJustificationDate> generalJustifications = justificationManager.findGeneralJustificationDateBy(start, end);

			// ordeno los usuarios
			
			PersonSort.sort(personsToReport);
			
			// genero el reporte.
			ReportSummary as = new ReportSummary();
			as.setStart(start);
			as.setEnd(end);
			
			for (Person p : personsToReport) {
				try {
					
					List<JustificationDate> justifications = justificationManager.findBy(Arrays.asList(p), start, end);			
		            List<Period> periodsAux = periodsManager.findAll(p, start, end, onlyWorkDays);
		            if (periodsAux == null) {
		            	continue;
		            }
		            
					Collections.sort(periodsAux, new PeriodComparator());		            
		            
					
					List<Group> groups = groupsManager.findByPerson(p);
					Group groupToSet = null;
					if (groups != null) {
						for (Group g : groups) {
							if (g.getTypes() != null && g.getTypes().contains(GroupType.OFFICE)) {
								groupToSet = g;
								break;
							}
						}
					}
					

		            for (Period period : periodsAux) {

		            	Report r = new Report();
		            	r.setPerson(p);
		            	r.setPeriod(period);
		            	r.setGroup(groupToSet);
		            	
		            	List<GeneralJustificationDate> jgds = checkGeneralJustification(generalJustifications, period);
		            	for (GeneralJustificationDate j : jgds) {
		            		r.getGjustifications().add(j);
		            	}
		            	
		            	List<JustificationDate> jds = checkJustification(justifications, period);
		            	for (JustificationDate j : jds) {
		            		r.getJustifications().add(j);
		            	}

		            	as.addReport(r);
		            }
					
					
				} catch (JustificationException | PeriodException | PersonException e) {
					logger.log(Level.WARNING,e.getMessage(),e);
					// error realizando el reporte. ignoro y sigo con otra
					// hay que ver por que se realizo el error.
				} catch (Exception e) {
					logger.log(Level.WARNING,e.getMessage(),e);
				}
			}
			
			as.calcReportData();
			return as;
			
		} catch (JustificationException e) {
			throw new IOException(e);
			
		}
	}
	
	private List<GeneralJustificationDate> checkGeneralJustification(List<GeneralJustificationDate> justifications, Period p) {
		Date startP = p.getStart();
		Date endP = p.getEnd();
		
		List<GeneralJustificationDate> jds = new ArrayList<>();
		
		for (GeneralJustificationDate jd : justifications) {
			Date startJ = jd.getStart();
			Date endJ = jd.getEnd();
			int start = startJ.compareTo(startP);
			int end = endJ.compareTo(endP);
			
			if (start <= 0 && end >= 0) {
				jds.add(jd);
			}
		}
		
		return jds;
	}
	
	private List<JustificationDate> checkJustification(List<JustificationDate> justifications, Period p) {
		Date startP = p.getStart();
		Date endP = p.getEnd();
		
		List<JustificationDate> jds = new ArrayList<>();
		
		for (JustificationDate jd : justifications) {
			Date startJ = jd.getStart();
			Date endJ = jd.getEnd();
			int start = startJ.compareTo(startP);
			int end = endJ.compareTo(endP);
			
			if (start <= 0 && end >= 0) {
				jds.add(jd);
			}
		}
		
		return jds;
	}

	
	
	
	
}
