package ar.com.dcsys.gwt.mapau.server.sileg;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.SilegFactory;
import ar.com.dcsys.gwt.mapau.shared.SilegMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.model.assignment.AssignmentsManager;
import ar.com.dcsys.model.silabouse.CoursesManager;


public class FindAllTeachersHandlers extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindAllTeachersHandlers.class.getName());
	
	private final SilegFactory sf;
	private final MessageUtils messageUtils;
	private final AssignmentsManager assignmentsManager;
	private final CoursesManager coursesManager;
	private final PersonEncoderDecoder personEncoderDecoder;
	
	@Inject
	public FindAllTeachersHandlers(SilegFactory sf, 
									   MessageUtils messageUtils,
									   PersonEncoderDecoder personEncoderDecoder,
									   AssignmentsManager assignmentsManager,
									   CoursesManager coursesManager) {
		this.messageUtils = messageUtils;
		this.sf = sf;
		this.assignmentsManager = assignmentsManager;
		this.personEncoderDecoder = personEncoderDecoder;
		this.coursesManager = coursesManager;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Override
	public boolean handles(Method method) {
		return SilegMethods.findAllTeachers.equals(method.getName());
	}

	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		
		try {
			List<Person> persons = new ArrayList<>();
			List<Course> courses = coursesManager.findAll();
			for (Course c : courses) {
				List<Person> teachers = assignmentsManager.getTeachersBy(c);
				persons.addAll(teachers);
			}
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
