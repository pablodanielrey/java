package ar.com.dcsys.web.document;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.exceptions.DocumentException;
import ar.com.dcsys.exceptions.DocumentNotFoundException;
import ar.com.dcsys.model.DocumentsManager;

@WebServlet("/documents/*")
public class DocumentsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject private DocumentsManager documentsManager;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		
		String token = req.getParameter("t");

		try {
			Document d = documentsManager.findById(token);
			
			String content = "";
			if (d.getMimeType() != null && (!d.getMimeType().equals(""))) {
				content = d.getMimeType();
			}
			
			if ((!content.equals("")) && d.getEncoding() != null && (!d.getEncoding().equals(""))) {
				content = content + ";charset=" + d.getEncoding();
			}
			
			resp.setContentType(content);
			
			OutputStream out = resp.getOutputStream();
			out.write(d.getContent());
			
		} catch (DocumentNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			
		} catch (DocumentException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
