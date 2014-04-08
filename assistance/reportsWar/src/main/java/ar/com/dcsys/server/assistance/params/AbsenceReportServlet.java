package ar.com.dcsys.server.assistance.params;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reporte/ausencias/*")
public class AbsenceReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
		
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {

		String groupId = req.getParameter("group");
		String personId = req.getParameter("person");
		
		if (groupId == null && personId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);			
			return;
		}
		
		String url = "";
		
		if (groupId != null) {
			if (groupId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000").toString())) {
				url = "/reporte/allgrupos/ausencias";
			} else {
				url = "/reporte/grupos/ausencias";
			}
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(req, resp);

	}
	
}
