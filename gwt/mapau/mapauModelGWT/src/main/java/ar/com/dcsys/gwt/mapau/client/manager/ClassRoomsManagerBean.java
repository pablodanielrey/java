package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomMethods;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsFactory;
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
	private final ClassRoomsFactory classRoomsFactory;
	private final ClassRoomsEncoderDecoder encoderDecoder;
	private final MessageUtils messageUtils;
	private final WebSocket socket;

	@Inject
	public ClassRoomsManagerBean(MapauFactory mapauFactory,
								 ClassRoomsFactory classRoomsFactory,
								 ClassRoomsEncoderDecoder encoderDecoder,
								 MessageUtils messageUtils,
								 WebSocket ws) {
		this.socket = ws;
		this.encoderDecoder = encoderDecoder;
		this.messageUtils = messageUtils;
		this.mapauFactory = mapauFactory;
		this.classRoomsFactory = classRoomsFactory;
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
	public void create(ClassRoom classRoom, CharacteristicQuantity c, final Receiver<Void> rec) {
		try {
			String[] params = new String[2]; 
			params[0] = ManagerUtils.encode(classRoomsFactory, ClassRoom.class, classRoom);
			params[1] = ManagerUtils.encode(classRoomsFactory, CharacteristicQuantity.class, c);
			String sparams = ManagerUtils.encodeParams(params);
			
			Message msg = messageUtils.method(ClassRoomMethods.createCharacteristicQuantity, sparams);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					try {
						rec.onSuccess(null);
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
	public void findAllCharacteristics(final Receiver<List<Characteristic>> rec) {
		try {
			Message msg = messageUtils.method(ClassRoomMethods.findAllCharacteristics);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}

					List<Characteristic> characts = null;
					try {
						String list = response.getPayload();
						characts = encoderDecoder.decodeCharacteristicList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(characts);
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
	public void findAllClassRooms(final Receiver<List<ClassRoom>> rec) {
		try {
			Message msg = messageUtils.method(ClassRoomMethods.findAllClassRooms);
			
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
						return;
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
