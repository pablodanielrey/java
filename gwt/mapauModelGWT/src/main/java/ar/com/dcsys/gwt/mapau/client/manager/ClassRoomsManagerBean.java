package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

import com.google.inject.Inject;

public class ClassRoomsManagerBean implements ClassRoomsManager {

	private static final Logger logger = Logger.getLogger(ClassRoomsManagerBean.class.getName());
	
	private final MapauFactory mapauFactory;
	private final ClassRoomsEncoderDecoder encoderDecoder;
	private final MessageUtils messageUtils;
	private final WebSocket socket;

	@Inject
	public ClassRoomsManagerBean(MapauFactory mapauFactory, 
								 ClassRoomsEncoderDecoder encoderDecoder,
								 MessageUtils messageUtils,
								 WebSocket ws) {
		this.socket = ws;
		this.encoderDecoder = encoderDecoder;
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
	public void findAllClassRooms(final Receiver<List<ClassRoom>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findAllClassRooms);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<ClassRoom> classRooms = null;
					try {
						String list = response.getPayload();
						classRooms = encoderDecoder.decodeClassRoomList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(classRooms);
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
