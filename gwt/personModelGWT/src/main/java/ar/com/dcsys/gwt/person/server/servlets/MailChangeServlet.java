/**
 * Servlet que maneja la operación de confirmación de los cambios de mails.
 * se ejecuta cuando se confirma un token generado en el modelo del sistema.
 */
package ar.com.dcsys.gwt.person.server.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.MailChangesManager;

@WebServlet("/mailChanges/*")
public class MailChangeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MailChangeServlet.class.getName());
	
	private final MailChangesManager changesManager;
	private final BeanManager beanManager;
	private final MessageUtils messageUtils;
	private String changeMailTemplate; 
	private String changeMailErrorTemplate;
	
	
	@Inject
	public MailChangeServlet(MailChangesManager changesManager, BeanManager beanManager, MessageUtils messageUtils) {
		super();
		this.changesManager = changesManager;
		this.beanManager = beanManager;
		this.messageUtils = messageUtils;

		changeMailTemplate = "<html><head></head><body>operación procesada correctamente</body></html>";
		changeMailErrorTemplate = "<html><head></head><body>error procesando operación</body></html>";
	}
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			String path = getServletContext().getRealPath("template-change-mail-error.html");
			changeMailErrorTemplate = fileToString(path);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e); 
		}
		
		try {
			String path = getServletContext().getRealPath("template-change-mail.html");
			changeMailTemplate = fileToString(path);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e); 
		}
	
	}
	
	private String fileToString(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		String template = Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
		return template;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {

		try {
			Object t = req.getParameter("t");
			if (t == null || !(t instanceof String)) {
				publishError(req,resp);
				return;
			}
			
			String sToken = (String)t;
			changesManager.processByToken(sToken);
			
			publishResponse(req,resp);
			
			Message msg = messageUtils.event(PersonMethods.mailChangeModifiedEvent,"");
			beanManager.fireEvent(msg);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			publishError(req,resp);
		}
	}
	
	
	private void publishError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer html = new StringBuffer();
		html.append(changeMailErrorTemplate);
		
		OutputStream out = resp.getOutputStream();
		PrintWriter pw = new PrintWriter(out);
		pw.print(html.toString());
		pw.flush();
	}

	
	private void publishResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer html = new StringBuffer();
		html.append(changeMailTemplate);
		
		OutputStream out = resp.getOutputStream();
		PrintWriter pw = new PrintWriter(out);
		pw.print(html.toString());
		pw.flush();
	}
	
	
}
