package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;



public class SilegManagerBean implements SilegManager {

	
	
	private static final Logger logger = Logger.getLogger(SilegManagerBean.class.getName());
	
	private final MapauFactory mapauFactory;
	private final SilegEncoderDecoder encoderDecoder;
	private final PersonEncoderDecoder pEncoderDecoder;
	private final MessageUtils messageUtils;
	private final WebSocket socket;

	@Inject
	public SilegManagerBean(MapauFactory mapauFactory, 
							SilegEncoderDecoder encoderDecoder,
							PersonEncoderDecoder pEncoderDecoder,
							MessageUtils messageUtils,
							WebSocket ws) {
		this.socket = ws;
		this.encoderDecoder = encoderDecoder;
		this.pEncoderDecoder = pEncoderDecoder;
		this.messageUtils = messageUtils;
		this.mapauFactory = mapauFactory;
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
	public void findAllCourses(final Receiver<List<Course>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findAllCourses);
			
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
			Message msg = messageUtils.method(MapauMethods.findAllTeachers);
			
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
		}	}

}
