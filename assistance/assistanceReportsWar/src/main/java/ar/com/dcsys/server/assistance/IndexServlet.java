package ar.com.dcsys.server.assistance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;

@WebServlet(urlPatterns={"/","/absence"})
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static String NULLGROUP= "00000000-0000-0000-0000-000000000000"; 
	
	
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;

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
			
			String groupId = req.getParameter("group");
			if (groupId == null) {

				// se esta tratando de mostrar el index.
				
				Group g = new Group();
				g.setId(UUID.fromString(NULLGROUP).toString());
				g.setName("Todos");
				
				List<Group> groups = new ArrayList<>();
				groups.add(g);
				groups.addAll(groupsManager.findAll());
				req.setAttribute("groups", groups);
				
				req.getRequestDispatcher("/index.jsp").forward(req, resp);
				
			} else {
				
				if (NULLGROUP.equals(groupId)) {
					
					/*
					 * aca tengo que generar reporte para cada uno de todos los grupos existentes en el sistema.
					 */
					resp.sendError(HttpServletResponse.SC_FORBIDDEN);
					
				} else {
					
					// solo el grupoId.
					req.getRequestDispatcher("/group").forward(req, resp);
				}
				
			}
			
		} catch (PersonException e) {
			throw new ServletException(e);
		}

		
	}
	
	
}
