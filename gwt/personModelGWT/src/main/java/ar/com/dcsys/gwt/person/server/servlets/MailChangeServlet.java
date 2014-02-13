package ar.com.dcsys.gwt.person.server.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
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
	
	
	@Inject
	public MailChangeServlet(MailChangesManager changesManager, BeanManager beanManager, MessageUtils messageUtils) {
		super();
		this.changesManager = changesManager;
		this.beanManager = beanManager;
		this.messageUtils = messageUtils;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {

		try {
			Message msg = messageUtils.event(PersonMethods.mailChangeModifiedEvent,"");
			beanManager.fireEvent(msg);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
}
