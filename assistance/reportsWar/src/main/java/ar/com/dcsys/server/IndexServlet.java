package ar.com.dcsys.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
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
			List<Group> groups = groupsManager.findAll();
			if (groups != null) {
				req.setAttribute("groups", groups);
			} else {
				req.setAttribute("groups", new ArrayList<Group>());
			}
		} catch (PersonException e) {
			throw new ServletException(e);
		}

		
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	
}
