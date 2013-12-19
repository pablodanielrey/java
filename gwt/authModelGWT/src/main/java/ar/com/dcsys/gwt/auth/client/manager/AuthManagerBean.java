package ar.com.dcsys.gwt.auth.client.manager;

import javax.inject.Inject;

import ar.com.dcsys.gwt.auth.shared.AuthMethods;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;
import ar.com.dcsys.gwt.ws.shared.SocketException;

import com.google.gwt.event.shared.EventBus;

public class AuthManagerBean implements AuthManager {

	private final ManagerUtils managerUtils;
	private final MessageUtils messageUtils;
	private final WebSocket socket;
	
	@Inject
	public AuthManagerBean(EventBus eventBus, 
			  ManagerUtils managerUtils,
			  MessageUtils messageUtils, 
			  WebSocket ws) {
		
		this.managerUtils = managerUtils;
		this.messageUtils = messageUtils;
		this.socket = ws;
	}
	
	@Override
	public void login(String username, String password, Receiver<Void> rec) {
	}

	@Override
	public void logout(Receiver<Void> rec) {
	}

	@Override
	public void isAuthenticated(Receiver<Boolean> rec) {
		
		Message msg = messageUtils.method(AuthMethods.isAuthenticated);
		
		try {
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message message) {
				}
				@Override
				public void onFailure(Throwable t) {
				}
			});
			
		} catch (SocketException e) {
			rec.onFailure(e);
		}
		
	}

	@Override
	public void hasPermission(String perm, Receiver<Boolean> rec) {
		// TODO Auto-generated method stub
		
	}

	
	
}
