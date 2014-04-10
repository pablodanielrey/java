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
import ar.com.dcsys.model.reports.ReportsGenerator;

@WebServlet("/personas")
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
		
		resp.setHeader("content-disposition", "attachment; filename=\"personas.pdf\"");
		OutputStream out = resp.getOutputStream();
		
		try {
			reportsGenerator.reportPersons(out);
			
		} catch (PersonException e) {
			throw new ServletException(e);
		}

		out.flush();
		out.close();
		
	}
}
