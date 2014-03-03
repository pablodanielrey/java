package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.SilegFactory;
import ar.com.dcsys.gwt.mapau.shared.SilegMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;



public class SilegManagerBean implements SilegManager {

	private static final Logger logger = Logger.getLogger(SilegManagerBean.class.getName());
	
	private final SilegFactory sf;
	private final SilegEncoderDecoder encoderDecoder;
	private final PersonEncoderDecoder pEncoderDecoder;
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final WebSocket socket;

	@Inject
	public SilegManagerBean(SilegFactory sf, 
							SilegEncoderDecoder encoderDecoder,
							PersonEncoderDecoder pEncoderDecoder,
							ManagerUtils managerUtils,
							MessageUtils messageUtils,
							WebSocket ws) {
		this.sf = sf;
		this.socket = ws;
		this.encoderDecoder = encoderDecoder;
		this.pEncoderDecoder = pEncoderDecoder;
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
	}
	
	private boolean handleError(Message response, Receiver<?> receiver) {
		if (MessageType.ERROR.equals(response.getType())) {
			String error = response.getPayload();
			receiver.onFailure(new MessageException(error));
			return true;
		}		
		return false;
	}		
	

	@Override
	public void findTeachers(Course c, final Receiver<List<Person>> rec) {
		try {
			String scourse = ManagerUtils.encode(sf, Course.class, c);
			Message msg = messageUtils.method(SilegMethods.findTeachersByCourse,scourse);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Person> persons = null;
					try {
						String list = response.getPayload();
						persons = pEncoderDecoder.decodePersonList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(persons);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}
	}
	
	@Override
	public void findAllCourses(final Receiver<List<Course>> rec) {
		try {
			Message msg = messageUtils.method(SilegMethods.findAllCourses);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Course> courses = null;
					try {
						String list = response.getPayload();
						courses = encoderDecoder.decodeCourseList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(courses);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}

	@Override
	public void findAllTeachers(final Receiver<List<Person>> rec) {
		try {
			Message msg = messageUtils.method(SilegMethods.findAllTeachers);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Person> persons = null;
					try {
						String list = response.getPayload();
						persons = pEncoderDecoder.decodePersonList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(persons);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}	
	}

}
