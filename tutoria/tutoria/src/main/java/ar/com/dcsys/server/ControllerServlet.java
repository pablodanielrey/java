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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.auth.AuthManager;
import ar.com.dcsys.server.tutoria.TutoriaManager;

@WebServlet(urlPatterns={"/","/index"})
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
	
	private static final String[] situations = {"Situaciones Administrativas",
												"Situaciones Académicas y de Estudio",
												"Situaciones Personales",
												"Situaciones Económicas"};
	
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			req.setCharacterEncoding("UTF-8");
			
			
			String sdate = req.getParameter("date");
			String sn = req.getParameter("studentNumber");
			String situation = req.getParameter("situation");
			String unknown = req.getParameter("unknown");
			
			if (unknown != null && unknown.equals("true")) {
				sn = null;
			}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			if (sdate != null) {
				try {
					date = df.parse(sdate);
				} catch (Exception e) {
					// nada la ignoro. date queda == null
				}
			}
			
			
			// por el maldito encoding tuve que usar esto.
			try {
				Integer index = Integer.parseInt(situation);
				situation = situations[index];
				
			} catch (Exception e) {
				situation = null;
			}
			
			if (date != null && situation != null) {
				
				
				// agrego la situación
				String id = getLoggedUserId();
				try {
					tutoriaManager.add(id, date, sn, situation);
					req.setAttribute("message", "Registro agregado exitósamente");
					
				} catch (Exception e) {
					req.setAttribute("message", "Error agregando registro, chequee los datos ingresados");	
				}
				
			}
			
			req.setAttribute("situations", situations);

			if (date == null) {
				date = new Date();
			}
			sdate = df.format(date); 
			req.setAttribute("date",sdate);
			
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new ServletException(e);
		}
	}

	private String getLoggedUserId() {
		Subject currentUser = SecurityUtils.getSubject();
		IdPrincipal p = (IdPrincipal)currentUser.getPrincipals().getPrimaryPrincipal();
		return p.getName();
	}
	
	
}
