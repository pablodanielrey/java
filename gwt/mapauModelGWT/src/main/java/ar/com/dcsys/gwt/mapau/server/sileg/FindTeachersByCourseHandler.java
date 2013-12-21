package ar.com.dcsys.gwt.mapau.server.sileg;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.SilegFactory;
import ar.com.dcsys.gwt.mapau.shared.SilegMethods;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.model.assignment.AssignmentsManager;


public class FindTeachersByCourseHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindTeachersByCourseHandler.class.getName());
	
	private final SilegFactory sf;
	private final MessageUtils messageUtils;
	private final AssignmentsManager assignmentsManager;
	private final PersonEncoderDecoder personEncoderDecoder;
	
	@Inject
	public FindTeachersByCourseHandler(SilegFactory sf, 
									   MessageUtils messageUtils,
									   PersonEncoderDecoder personEncoderDecoder,
									   AssignmentsManager assignmentsManager) {
		this.messageUtils = messageUtils;
		this.sf = sf;
		this.assignmentsManager = assignmentsManager;
		this.personEncoderDecoder = personEncoderDecoder;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Override
	public boolean handles(Method method) {
		return SilegMethods.findTeachersByCourse.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String params = method.getParams();
			Course course = ManagerUtils.decode(sf, Course.class, params);
			List<Person> persons = assignmentsManager.getTeachersBy(course);
			String lpersons = personEncoderDecoder.encodePersonList(persons);
			sendResponse(msg, transport, lpersons);		
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
