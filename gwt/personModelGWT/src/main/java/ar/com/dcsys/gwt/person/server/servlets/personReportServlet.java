package ar.com.dcsys.gwt.person.server.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.server.reports.ReportContainer;

@WebServlet("/personReport/*")
public class personReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(personReportServlet.class.getName());
	
	private final BeanManager beanManager;
	private final MessageUtils messageUtils;
	private final ReportContainer reportContainer;
	
	
	@Inject
	public personReportServlet(BeanManager beanManager, MessageUtils messageUtils, ReportContainer reportContainer) {
		super();
		this.beanManager = beanManager;
		this.messageUtils = messageUtils;
		this.reportContainer = reportContainer;
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
			byte[] data = reportContainer.getReport(sToken);
			if (data == null) {
				publishError(req, resp);
				return;
			}
			
			OutputStream out = resp.getOutputStream();
			out.write(data);
			out.flush();
			
/*			
			Message msg = messageUtils.event(PersonMethods.mailChangeModifiedEvent,"");
			beanManager.fireEvent(msg);
*/
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			publishError(req,resp);
		}
	}
	
	
	private void publishError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer html = new StringBuffer();
		html.append("<html><head></head><body>ERROR</body></html>");
		
		OutputStream out = resp.getOutputStream();
		PrintWriter pw = new PrintWriter(out);
		pw.print(html.toString());
		pw.flush();
	}

}
