package ar.com.dcsys.server.person;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.JustificationException;
import ar.com.dcsys.exceptions.PeriodException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.justification.JustificationsManager;
import ar.com.dcsys.model.period.Period;
import ar.com.dcsys.model.period.PeriodsManager;
import ar.com.dcsys.model.period.WorkedHours;
import ar.com.dcsys.utils.PersonSort;


@WebServlet("/report/*")
public class PersonReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject PeriodsManager periodsManager;
	@Inject JustificationsManager justificationManager;
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	
	private final static ConstantsBean constants = new ConstantsBean();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
		
			// se obtienen las fechas del reporte
			SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");
			String pstart = req.getParameter("start");
			String pend = req.getParameter("end");
			if (pstart == null || pend == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			Date start = format.parse(req.getParameter("start"));
			Date end = format.parse(req.getParameter("end"));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(end);
			cal.set(Calendar.HOUR_OF_DAY,23);
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			end = cal.getTime();
			
			
			// se determina a quien se le va a ahcer el reporte.
			String groupId = req.getParameter("group");
			String personId = req.getParameter("person");
			if (personId == null && groupId == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			List<Person> personsToReport = new ArrayList<Person>();
			
			if (groupId != null) {
				Group group = groupsManager.findByIdEager(groupId);
				if (group == null) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				personsToReport.addAll(group.getPersons());
			} else {
				Person person = personsManager.findById(personId);
				if (person == null) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				personsToReport.add(person);
			}
			

			String reportType = req.getParameter("periodFilter");
			if (reportType == null) {
				reportType = constants.getAll();
			}
			
			// busco las justificaciones generales.
			
			List<GeneralJustificationDate> generalJustifications = justificationManager.findGeneralJustificationDateBy(start, end);			
			StringBuilder sb = new StringBuilder();

			// ordeno los usuarios
			
			PersonSort.sort(personsToReport);
			
			// genero el reporte.
			
			generateHeader(sb);
			for (Person p : personsToReport) {
				try {
					report(p,start,end,generalJustifications,sb, reportType);
				} catch (JustificationException | PeriodException | PersonException e) {
					// error realizando el reporte. ignoro y sigo con otra
					// hay que ver por que se realizo el error.
				}
				sb.append("\n");
			}
			
			/// envío el contenido del reporte.
			
			PrintWriter pout = ServletExportCSV.getWriter("reporte", resp);
			
			pout.println(sb.toString());
			pout.flush();
			pout.close();
		
		} catch (PersonException | JustificationException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (java.text.ParseException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private GeneralJustificationDate checkGeneralJustification(List<GeneralJustificationDate> justifications, Period p) {
		Date startP = p.getStart();
		Date endP = p.getEnd();
		
		for (GeneralJustificationDate jd : justifications) {
			Date startJ = jd.getStart();
			Date endJ = jd.getEnd();
			int start = startJ.compareTo(startP);
			int end = endJ.compareTo(endP);
			
			if (start <= 0 && end >= 0) {
				return jd;
			}
		}
		return null;
	}
	
	private JustificationDate checkJustification(List<JustificationDate> justifications, Period p) {
		Date startP = p.getStart();
		Date endP = p.getEnd();
		
		for (JustificationDate jd : justifications) {
			Date startJ = jd.getStart();
			Date endJ = jd.getEnd();
			int start = startJ.compareTo(startP);
			int end = endJ.compareTo(endP);
			
			if (start <= 0 && end >= 0) {
				return jd;
			}
		}
		return null;
	}
	
	
	/**
	 * Genera la cabecera del reporte.
	 * @param requester
	 * @param sb
	 */
	private void generateHeader(StringBuilder sb) {
		sb.append("APELLIDO;NOMBRE;DNI;CARGO;HORARIO;FECHA ENTRADA; HORA ENTRADA; HORA SALIDA;CANTIDAD DE HORAS TRABAJADAS;JUSTIFICACION;NOTAS;JUSTIFICACION GENERAL;NOTAS\n");
	}
	
	
	/**
	 * Genera el reporte de los períodos con estadísiticas semanales y mensuales.
	 * @param requester
	 * @param person
	 * @param start
	 * @param end
	 * @param resp
	 * @throws IOException
	 */
	private void report(Person person, Date start, Date end, List<GeneralJustificationDate> generalJustifications, StringBuilder sb, String reportType) throws JustificationException, PeriodException, PersonException {
		
					
			List<Period> periods = null;

//			PERIODFILTER filter = PERIODFILTER.valueOf(reportType);
			periods = periodsManager.findAll(person, start, end, false);
			
		
			if (periods == null) {
				periods = new ArrayList<Period>();
			}
			
			// ordeno los períodos. debe ser la lista ordenada ya que si no no funciona correctamente el reporte.
			Collections.sort(periods, new Comparator<Period>() {
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

			DateFormat onlyDate = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat onlyTime = new SimpleDateFormat("HH:mm:ss");
			
			//////////////////////////// datos de la persona ///////////////////////////////////////////
			
			sb.append(person.getLastName() == null ? "No Tiene" : person.getLastName());
			sb.append(";" + (person.getName() == null ? "No Tiene" : person.getName()));
			sb.append(";" + person.getDni());
			
			/////////////////////  grupos de la persona (horarios y cargos) ////////////////////////////
			
			Group tg = null;					// horario
			Group pg = null; 					// cargo
			List<Group> groups = groupsManager.findByPerson(person);
			for (Group g : groups) {
				if (g.getTypes().contains(GroupType.TIMETABLE)) {
					tg = g;
				}
				if (g.getTypes().contains(GroupType.POSITION)) {
					pg = g;
				}
				if (tg != null && pg != null) {
					break;
				}
			}
			
			sb.append(";" + (pg != null ? pg.getName() : "No tiene"));
			sb.append(";" + (tg != null ? tg.getName() : "No tiene"));
			
			
			/////////////////////// justificaciones de la persona en ese período ////////////////////////
			
			List<JustificationDate> justifications = justificationManager.findBy(Arrays.asList(person), start, end);
			
			/////////////////////// datos de los periodos ////////////////////////////////////////////////
			
			long totalMilis = 0;
			long weeklyMilis = 0;
			int periodsCount = 0;
			
			final long minute = 1000l * 60l;
			
			sb.append(";");
			
			
			for (Period p : periods) {

				periodsCount++;
				
				
				// FECHA ENTRADA; HORA ENTRADA; FECHA SALIDA; HORA SALIDA;CANTIDAD DE HORAS TRABAJADAS;
				
				List<WorkedHours> whs = p.getWorkedHours();
				if (whs == null || whs.size() <= 0) {
					// no trabajo, es una inasistencia. se toman las fechas del período.
					Date pstart = p.getStart();
					Date pend = p.getEnd();
					
					sb.append(onlyDate.format(pstart)).append(";;").append(";").append("00:00");

				} else {
					for (int i = 0; i < whs.size(); ) {
						WorkedHours wh = whs.get(i);
						
						if (wh.getInLog() != null) {
							AttLog log = wh.getInLog();
							sb.append(onlyDate.format(log.getDate())).append(";").append(onlyTime.format(log.getDate())).append(";");
						} else {
							sb.append(";;");
						}
						
						if (wh.getOutLog() != null) {
							AttLog log = wh.getOutLog();
							sb.append(onlyTime.format(log.getDate())).append(";");
						} else {
							sb.append(";");
						}
						
						long milis = (wh.getWorkedMilis() / (1000l * 60l));
						long hours = milis / 60l;
						long minutes = milis % 60l;
						
						sb.append(String.format("%02d",hours)).append(":").append(String.format("%02d", minutes));
						
						totalMilis += wh.getWorkedMilis();
						weeklyMilis += wh.getWorkedMilis();
						
						i++;
						
					}
				}
				
				
				// chequeo las justificaciones para ese período
				
				sb.append(";");
				// chequeo si esta justificado
				JustificationDate j = checkJustification(justifications,p);
				if (j != null) {
					sb.append(j.getJustification().getCode());
					sb.append(" ");
					sb.append(j.getJustification().getDescription());
					sb.append(";");
					if (j.getNotes() != null) {
						sb.append(j.getNotes());
					}
				} else {
					sb.append(";");
				}

				sb.append(";");
				// chequeo justificacion general
				GeneralJustificationDate gj = checkGeneralJustification(generalJustifications,p);
				if (gj != null) {
					sb.append(gj.getJustification().getCode());
					sb.append(" ");
					sb.append(gj.getJustification().getDescription());
					sb.append(";");
					if (gj.getNotes() != null) {
						sb.append(gj.getNotes());
					}
				} else {
					sb.append(";");
				}

				sb.append("\n;;;;;");				
				
				
				/// chequeo el tema de los 7 días, etc.
			/*	
				if (periodsCount >= summaryDays) {
					// imprimo el conteo semanal.
					long whours = weeklyMilis / (60l * minute);
					long whoursMilis = whours * 60l * minute;
					long wminutes = (weeklyMilis - whoursMilis) / minute;
					
					sb.append(";;;;;;;;;").append(String.format("%02d",whours)).append(":").append(String.format("%02d",wminutes));

					periodsCount = 0;
					weeklyMilis = 0;
					
					sb.append("\n;;;;;");					
				}*/				
				
			}
			
			
			// imprimo el restante de la suma semanal. (el resto de los días que es menor que sumaryDays))
			
			/*if (periodsCount < summaryDays && periodsCount > 0) {
				// imprimo el conteo semanal.
				long whours = weeklyMilis / (60l * minute);
				long whoursMilis = whours * 60l * minute;
				long wminutes = (weeklyMilis - whoursMilis) / minute;
				
				sb.append(";;;;;;;;;").append(String.format("%02d",whours)).append(":").append(String.format("%02d",wminutes));

				periodsCount = 0;
				weeklyMilis = 0;
				
				sb.append("\n;;;;;");					
			}	*/			
			
			
			
			/*
			// imprimo el total de horas trabajadas.
			long thours = totalMilis / (60l * minute);
			long thoursMilis = thours * 60l * minute;
			long tminutes = (totalMilis - thoursMilis) / minute;
			
			String shours = String.format("%02d",thours);
			String sminutes = String.format("%02d",tminutes);
			sb.append(";;;;;;;;;;").append(shours).append(":").append(sminutes);*/			
			
			sb.append("\n;;;;;");
	}
	
}
