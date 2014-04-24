package ar.com.dcsys.server.assistance;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.reports.ReportExportType;
import ar.com.dcsys.model.reports.assistance.ReportGenerator;


@WebServlet("/group")
public class GroupAbsenceReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
			
			
			// se determina a quien se le va a ahcer el reporte.
			String groupId = req.getParameter("group");
			String personId = req.getParameter("person");
			if (groupId == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			Group group = null;
			group = groupsManager.findByIdEager(groupId);
			if (group == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			String reportType = req.getParameter("periodFilter");
			if (reportType == null) {
				reportType = constants.getAll();
			}
			
			ReportExportType type = ReportExportType.PDF;
			String exportType = req.getParameter("exportType");
			if ("pdf".equalsIgnoreCase(exportType)) {
				type = ReportExportType.PDF;
			} else if ("docx".equalsIgnoreCase(exportType)) {
				type = ReportExportType.DOCX;
			} else if ("xls".equalsIgnoreCase(exportType)) {
				type = ReportExportType.EXCEL;
			} else if ("csv".equalsIgnoreCase(exportType)) {
				type = ReportExportType.CSV;
			}			
			

			if (group == null || group.getName() == null) {
			resp.setHeader("content-disposition", "attachment; filename=\"ausencias-grupos." + type + "\"");
			} else {
				resp.setHeader("content-disposition", "attachment; filename=\"ausencias-grupo-" + group.getName() + "." + type + "\"");
			}
			OutputStream out = resp.getOutputStream();
			

			
			if (reportType.equals(constants.getAll())) {
				reportGenerator.reportAbsences(out, start, end, group, type);				
			} else if (reportType.equals(constants.getInjustificatedAbsences())) {
				reportGenerator.reportInjustificatedAbsences(out, start, end, group, type);
			} else if (reportType.equals(constants.getJustificatedAbsences())) {
				reportGenerator.reportJustificatedAbsences(out, start, end, group, type);
			}
		
			
			
			
			out.flush();
			out.close();
		
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
}
