package ar.com.dcsys.server.shortener;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;

public class RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	@Inject RedirDAO redirDao;

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
			
			Object otoken = req.getAttribute("token");
			if (otoken != null) {
				
				String token = (String)otoken;
				if (!token.trim().equals("")) {

					process(req,resp,token);
					return;
					
				}
			}
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp, String token) throws ServletException, IOException {
		
		try {
			Redir r = redirDao.findByUrl(token);
			String url = r.getRedir();
			resp.sendRedirect(url);
			
		} catch (IOException e) {
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			
		}
		
	}
	
}
