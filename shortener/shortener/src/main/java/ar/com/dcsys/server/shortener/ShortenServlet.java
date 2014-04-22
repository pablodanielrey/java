package ar.com.dcsys.server.shortener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;

public class ShortenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Inject PersonsManager personsManager;
	@Inject GroupsManager groupsManager;
	@Inject RedirDAO redirDAO;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	
	private static final char[] codes = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z',
										 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z',
										 '0','1','2','3','4','5','6','7','8','9' };
	
	
	private static final int indexOf(char c) {
		int i = 0;
		while (codes[i] != c) {
			i++;
		}
		return i;
	}
	
	private static final char charAt(int i) {
		return codes[i];
	}
	
	
	
	private static String genRedir(String a) {
		
		StringBuffer sb = new StringBuffer(a);
		
		// busco el caracter a tratar.
		int i = 0;
		for (i = 0; i < a.length(); i++) {
			char t = sb.charAt(i);
			int index = indexOf(t);
			int replace = (index + 1) % codes.length;
			if (replace > index) {

				index = index + 1;
				sb.deleteCharAt(i);
				sb.insert(i, charAt(index));
				return sb.toString();
				
			}
			
		}

		sb = new StringBuffer();
		for (i = 0; i <= a.length(); i++) {
			sb.append("a");
		}
		return sb.toString();
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			// proceso el pedido de generar una redireccion
			
			String url = req.getParameter("url");
			if (url != null) {
				
				// genero la redireccion.
				List<Redir> redirs = redirDAO.findAll();
				if (redirs == null || redirs.size() <= 0) {
					
					Redir r = new Redir(genRedir("a"),url);
					redirDAO.persist(r);
					redirs = Arrays.asList(r);
					
				} else {
					
					Redir lastR = redirs.get(redirs.size() - 1);
					String rurl = lastR.getUrl();
					Redir newRedir = new Redir(genRedir(rurl),url);
					redirDAO.persist(newRedir);
					redirs.add(newRedir);
					
				}
				
				req.setAttribute("urls", redirs);
				req.getRequestDispatcher("/index.jsp").forward(req, resp);

				return;
			}
			
			// ni genere, ni procese
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}
	
}
