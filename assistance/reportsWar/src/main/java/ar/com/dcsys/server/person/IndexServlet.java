package ar.com.dcsys.server.person;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.reports.ReportExportType;
import ar.com.dcsys.model.reports.ReportsGenerator;

@WebServlet("/personas/*")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject ReportsGenerator reportsGenerator;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String ext = "xls";
		
		String uri = req.getRequestURI();
		if (uri.endsWith("pdf")) {
			ext = "pdf";
		} else if (uri.endsWith("excel")) {
			ext = "xls";
		} else if (uri.endsWith("docx")) {
			ext = "docx";
		} else if (uri.endsWith("csv")) {
			ext = "csv";
		}
		
		resp.setHeader("content-disposition", "attachment; filename=\"personas." + ext + "\"");
		OutputStream out = resp.getOutputStream();
		
		try {
			
			
			if (uri.endsWith("/personas/oficinas")) { 
				reportsGenerator.reportPersonsByOffice(out,ReportExportType.EXCEL);
			} else if (uri.endsWith("/personas/oficinas/excel")) { 
				reportsGenerator.reportPersonsByOffice(out,ReportExportType.EXCEL);
			} else if (uri.endsWith("/personas/oficinas/pdf")) { 
				reportsGenerator.reportPersonsByOffice(out,ReportExportType.PDF);
			} else if (uri.endsWith("/personas/oficinas/docx")) { 
				reportsGenerator.reportPersonsByOffice(out,ReportExportType.DOCX);
			} else if (uri.endsWith("/personas/oficinas/csv")) { 
				reportsGenerator.reportPersonsByOffice(out,ReportExportType.CSV);
				
			} else if (uri.endsWith("/personas/cargos")) {
				reportsGenerator.reportPersonsByPosition(out,ReportExportType.EXCEL);
			} else if (uri.endsWith("/personas/cargos/excel")) {
				reportsGenerator.reportPersonsByPosition(out,ReportExportType.EXCEL);
			} else if (uri.endsWith("/personas/cargos/pdf")) {
				reportsGenerator.reportPersonsByPosition(out,ReportExportType.PDF);
			} else if (uri.endsWith("/personas/cargos/docx")) {
				reportsGenerator.reportPersonsByPosition(out,ReportExportType.DOCX);
			} else if (uri.endsWith("/personas/cargos/csv")) {
				reportsGenerator.reportPersonsByPosition(out,ReportExportType.CSV);
				
			} else if (uri.endsWith("/personas/excel")) {
				reportsGenerator.reportPersons(out,ReportExportType.EXCEL);
			} else if (uri.endsWith("/personas/pdf")) {
				reportsGenerator.reportPersons(out,ReportExportType.PDF);
			} else if (uri.endsWith("/personas/docx")) {
				reportsGenerator.reportPersons(out,ReportExportType.DOCX);
			} else if (uri.endsWith("/personas/csv")) {
				reportsGenerator.reportPersons(out,ReportExportType.CSV);
			} else {
				reportsGenerator.reportPersons(out,ReportExportType.EXCEL);
			}
			
		} catch (PersonException e) {
			throw new ServletException(e);
		}

		out.flush();
		out.close();
		
	}
}
