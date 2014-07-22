package ar.com.dcsys.model.device;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;

@ServerEndpoint(value="/devices")
public class DevicesWebsocket {

	public static final Logger logger = Logger.getLogger(DevicesWebsocket.class.getName());

	private final DevicesManager manager;
	
	@Inject
	public DevicesWebsocket(DevicesManager manager) {
		this.manager = manager;
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.fine("onOpen");
	}
	
	@OnMessage
	public void onMessage(String m, Session session) {
		logger.fine("Mensaje recibido : " + m);
		/*
		if (m.contains("enroll")) {
			try {
//				manager.enroll("personid-pablo", new EnrollMana);
				logger.fine("enrolado exitoso");
			
			} catch (PersonException | DeviceException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		*/
	}
	
	@OnClose
	public void onClose(Session s, CloseReason reason) {
		logger.fine("onClose");
	}
		 
	
	
}
