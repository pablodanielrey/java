package ar.com.dcsys.gwt.mapau.server.sileg;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.SilegMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.silabouse.CoursesManager;

public class FindAllCoursesHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAllCoursesHandler.class.getName());

	private final SilegEncoderDecoder silegEncoderDecoder;
	private final CoursesManager coursesManager;
	private final MessageUtils messageUtils;
	
	@Inject
	public FindAllCoursesHandler(MessageUtils messageUtils,
						  CoursesManager coursesManager,
						  SilegEncoderDecoder silegEncoderDecoder) {
		this.messageUtils = messageUtils;
		this.coursesManager = coursesManager;
		this.silegEncoderDecoder = silegEncoderDecoder;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}

	@Override
	public boolean handles(Method method) {
		return SilegMethods.findAllCourses.equals(method.getName());
	}

	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		
		try {
			List<Course> courses = coursesManager.findAll();
			String scourses = silegEncoderDecoder.encodeCourseList(courses);
			sendResponse(msg, transport, scourses);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
		
	}
	
	
	
}
