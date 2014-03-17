package ar.com.dcsys.server.person;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/report")
public class ReportTest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req,resp);
	}
	
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		OutputStream out = resp.getOutputStream();
		PrintWriter pout = new PrintWriter(out);
		
		
		String s = req.getParameter("group");
		if (s != null) {
			pout.println("<div>" + s + "</div>");
		}
		
		s = req.getParameter("person");
		if (s != null) {
			pout.println("<div>" + s + "</div>");
		}
		
		s = req.getParameter("periodFilter");
		if (s != null) {
			pout.println("<div>" + s + "</div>");
		}
		
		s = req.getParameter("start");
		if (s != null) {
			pout.println("<div>" + s + "</div>");
		}
		
		s = req.getParameter("end");
		if (s != null) {
			pout.println("<div>" + s + "</div>");
		}

		pout.flush();
	}
	
}
