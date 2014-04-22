package ar.com.dcsys.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String url = req.getParameter("url");
		if (url != null) {
			
			// genera la redireccion.
			req.getRequestDispatcher("shorten").forward(req, resp);
			return;
			
		} else if (uri.lastIndexOf("/") != (uri.length() - 1)) {
		
			// redirecciona
			String token = uri.substring(uri.lastIndexOf("/") + 1);
			req.setAttribute("token", token);
			req.getRequestDispatcher("redirect").forward(req,resp);
			return;
			
		} else {
			
			// muestro la pagina para generar las redirecciones.
			req.getRequestDispatcher("index.jsp").forward(req, resp);
			return;
			
		}
		
		
	}
	
}
