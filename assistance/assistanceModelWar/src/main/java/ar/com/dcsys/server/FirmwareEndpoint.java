package ar.com.dcsys.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ar.com.dcsys.assistance.server.AttLogSerializer;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.model.log.AttLogsManager;

@ServerEndpoint(value="/firmware")
public class FirmwareEndpoint {

	public static final Logger logger = Logger.getLogger(FirmwareEndpoint.class.getName());
	
	private final AttLogSerializer attLogSerializer;
	private final AttLogsManager attLogsManager;
	
	@Inject
	public FirmwareEndpoint(AttLogSerializer attLogSerializer, AttLogsManager attLogsManager) {
		this.attLogSerializer = attLogSerializer;
		this.attLogsManager = attLogsManager;
	}
	
	@OnOpen
	public void onOpen(Session s, EndpointConfig config) throws IOException {
		logger.log(Level.FINE, "onOpen : " + s.getId());
	}
	
	@OnMessage
	public void onMessage(String m, final Session session) {
		try {
			logger.log(Level.FINE,m);
	
			if (m.startsWith("attLog;")) {
				String attLogJson = m.replace("attLog;", "");
				AttLog attLog = attLogSerializer.read(attLogJson);
				attLogsManager.persist(attLog);
				
				session.getBasicRemote().sendText("OK");
				
			} else {
				logger.log(Level.SEVERE,"comando desconocido");
				session.getBasicRemote().sendText("ERROR");
				
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			try {
				session.getBasicRemote().sendText("ERROR");
			} catch (IOException e1) {
				logger.log(Level.SEVERE,e1.getMessage(),e1);
			}
			
		}
	}
	

	
	@OnError
	public void onError(Throwable t) {
		logger.log(Level.SEVERE,t.getMessage(),t);
	}

	@OnClose
	public void onClose(Session s, CloseReason reason) {
		logger.fine("onClose : " + s.getId());
	}	
	
}
