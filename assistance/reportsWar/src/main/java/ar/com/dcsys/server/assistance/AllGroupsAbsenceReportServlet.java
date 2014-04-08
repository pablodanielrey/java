package ar.com.dcsys.server.assistance;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.server.person.ConstantsBean;





@WebServlet("/reporte/allgrupos/ausencias/*")
public class AllGroupsAbsenceReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	@Inject ReportGenerator reportGenerator;
	
	private final static ConstantsBean constants = new ConstantsBean();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
		
			// se obtienen las fechas del reporte
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
			


			StyleBuilder bold = stl.style().bold();
			StyleBuilder boldCentered = stl.style(bold).setHorizontalAlignment(HorizontalAlignment.CENTER);
			
			StyleBuilder title = stl.style(boldCentered)
									.setVerticalAlignment(VerticalAlignment.MIDDLE)
									.setFontSize(15);
			
			StyleBuilder columnTitle = stl.style(boldCentered)
									.setBorder(stl.pen1Point())
									.setBackgroundColor(Color.LIGHT_GRAY);
			
			TextColumnBuilder<String> groupC = Columns.column("Grupo",".groupName", DataTypes.stringType());
			TextColumnBuilder<String> person = Columns.column("Nombre",".name", DataTypes.stringType());

			
			
			List<Person> personsToReport = personsManager.findAll();
			
//			resp.setContentType("multipart/x-mixed-replace;boundary=END");
			OutputStream out = resp.getOutputStream();
			
			String reportType = req.getParameter("periodFilter");
			if (reportType == null) {
				reportType = constants.getAll();
			}
			
			ReportSummary rs = reportGenerator.getReport(start, end, personsToReport);
			
			filterReport(rs, reportType);

			DynamicReports.report()
			.title(cmp.verticalList()
						.add(cmp.text("Ausencias").setStyle(title))
						.add(cmp.horizontalList()
								.add(cmp.text("Fecha Reporte :"))
								.add(cmp.currentDate())
								)
						.add(cmp.horizontalList()
								.add(cmp.text("Tipo Reporte"))
								.add(cmp.text(reportType))
								)
						.add(cmp.horizontalList()
								.add(cmp.text("Inicio"))
								.add(cmp.text(start))
								)
						.add(cmp.horizontalList()
								.add(cmp.text("Fin"))
								.add(cmp.text(end))
								)
						.add(cmp.filler().setFixedHeight(10))
						)
			.setColumnTitleStyle(columnTitle)
			.highlightDetailEvenRows()
			.columns(
					groupC,
					person,
					Columns.column("Fecha",".date",DataTypes.dateType())
						.setPattern("dd/MM/yyyy"),
					Columns.column("Justificación",".justification",DataTypes.stringType()),
					Columns.column("Justificación General", ".generalJustification", DataTypes.stringType())
			)
			.groupBy(groupC,person)
			.setDataSource(new ReportDataSource(rs))
			.toExcelApiXls(out);
			//.toPdf(out);

			out.flush();
			out.close();
			
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	/**
	 * Remuevo los reportes que no cumplen con el tipo de filtro requerido
	 * @param rs
	 * @param reportType
	 */
	private void filterReport(ReportSummary rs, String reportType) {
		
		List<Report> reportsToRemove = new ArrayList<>();

		if (reportType.equals(constants.getAll())) {
			
			for (Report r : rs.getReports()) {
				if (!r.isAbscence()) {
					reportsToRemove.add(r);
				}
			}
			
		} else if (reportType.equals(constants.getInjustificatedAbsences())) {
		
            for (Report r : rs.getReports()) {
            	if (!r.isAbscence()) {
            		
            		reportsToRemove.add(r); 

            	} else if ((r.getJustifications() != null && r.getJustifications().size() > 0) || 
            			  (r.getGjustifications() != null && r.getGjustifications().size() > 0)) {
            		
            		reportsToRemove.add(r);
            	}
            }
            
		} else if (reportType.equals(constants.getJustificatedAbsences())) {

            for (Report r : rs.getReports()) {
            	if (!r.isAbscence()) {
            		
            		reportsToRemove.add(r);
            		
            	} else if ((r.getJustifications() == null || r.getJustifications().size() <= 0) &&
            			  (r.getGjustifications() == null || r.getGjustifications().size() <= 0)) {
            		
            		reportsToRemove.add(r);
            	}
            }
			
		}
		
		rs.getReports().removeAll(reportsToRemove);
		
	}
	
	
	
	
	

			/*
			
			
			DateFormat onlyDate = new SimpleDateFormat("dd/MM/yyyy");
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
			
			
			*/

	
}
