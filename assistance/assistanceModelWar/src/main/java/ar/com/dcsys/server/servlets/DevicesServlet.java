package ar.com.dcsys.server.servlets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

@WebServlet("/devices")
public class DevicesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final PersonsManager personsManager;
	private final Enroller enroller;
	
	@Inject
	public DevicesServlet(PersonsManager personsManager, Enroller enroller) {
		this.personsManager = personsManager;
		this.enroller = enroller;
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		
		String function = req.getParameter("function");
		
		if (function == null) {
			String msg = enroller.getStatus();
			if (msg != null) {
				req.setAttribute("refresh",true);
				req.setAttribute("messages", msg);
			}
		}
		
		
		if ("getUsers".equals(function)) {
			getUsers(req, resp);
		}
		
		if ("enroll".equals(function)) {
			getUsers(req, resp);
			enroll(req,resp);
		}
		
		req.getRequestDispatcher("/devices.jsp").forward(req, resp);
//		resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		return;
		
	}
	
	private void getUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		
		try {
			List<Person> persons = personsManager.findAll();
			req.setAttribute("userList", persons);
			
		} catch (PersonException e) {
			throw new ServletException(e);
		}
		
		
	}

	
	private void enroll(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		
		String personId = req.getParameter("personId");
		
		enroller.enroll(personId);
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {

		}
		String msg = enroller.getStatus();
		req.setAttribute("refresh",true);
		req.setAttribute("messages", msg);
		
	}
	
}
