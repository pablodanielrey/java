package ar.com.dcsys.server.assistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.justification.JustificationsManager;
import ar.com.dcsys.model.period.Period;
import ar.com.dcsys.model.period.PeriodsManager;
import ar.com.dcsys.utils.PersonSort;

public class ReportGenerator {

	@Inject PeriodsManager periodsManager;
	@Inject JustificationsManager justificationManager;
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	
	
	public ReportSummary getReport(Date start, Date end, Group g) throws IOException {
		List<Person> persons = g.getPersons();
		ReportSummary rs = getReport(start,end,persons);
		rs.setGroup(g);
		return rs;
	}
	
	
	public ReportSummary getReport(Date start, Date end, List<Person> personsToReport) throws IOException {
	
		try {
		
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
		            List<Period> periodsAux = periodsManager.findAll(p, start, end, true);
		            
					Collections.sort(periodsAux, new Comparator<Period>() {
						@Override
						public int compare(Period p0, Period p1) {
							if (p0 == null && p1 == null) {
								return 0;
							}
							
							if (p0.getStart() == null && p1.getStart() == null) {
								return 0;
							}
							
							if (p0.getStart() == null) {
								return -1;
							}
							
							if (p1.getStart() == null) {
								return 1;
							}
							
							return (p0.getStart().compareTo(p1.getStart()));
						}
					});		            
		            

		            for (Period period : periodsAux) {

		            	Report r = new Report();
		            	r.setPerson(p);
		            	r.setPeriod(period);
		            	
		            	List<GeneralJustificationDate> jgds = checkGeneralJustification(generalJustifications, period);
		            	for (GeneralJustificationDate j : jgds) {
		            		r.getGjustifications().add(j.getJustification());
		            	}
		            	
		            	List<JustificationDate> jds = checkJustification(justifications, period);
		            	for (JustificationDate j : jds) {
		            		r.getJustifications().add(j.getJustification());
		            	}

		            	as.getReports().add(r);
		            }
					
					
				} catch (JustificationException | PeriodException | PersonException e) {
					// error realizando el reporte. ignoro y sigo con otra
					// hay que ver por que se realizo el error.
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
