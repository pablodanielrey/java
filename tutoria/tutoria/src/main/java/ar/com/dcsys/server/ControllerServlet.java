package ar.com.dcsys.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.auth.AuthManager;
import ar.com.dcsys.server.tutoria.TutoriaManager;

@WebServlet("/index")
public class ControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ControllerServlet.class.getName());
	
	@Inject TutoriaManager tutoriaManager;
	@Inject AuthManager authsManager;
	@Inject PersonsManager personsManager;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
		
			String sdate = req.getParameter("date");
			String sn = req.getParameter("studentNumber");
			String situation = req.getParameter("situation");
			
			if (sdate != null && sn != null && situation != null) {
				
				// agrego la situación
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = df.parse(sdate);
				
				tutoriaManager.add("1", date, sn, situation);
			}
			
			req.setAttribute("situations", new String[]{"Situaciones Administrativas",
														"Situaciones Académicas y de Estudio",
														"Situaciones Personales",
														"Situaciones Económicas"});
			
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new ServletException(e);
		}
	}
	
	
}
