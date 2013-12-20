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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.sun.org.apache.regexp.internal.RE;

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
	
	private String getAuthUrl() {
		return GWT.getHostPageBaseURL() + "AuthGWT.html";
	}
	
	/**
	 * por un bug que tiene shiro, todo debe ser procesador por el shiroFilter, si no no se inicia el subject.
	 * asi que llamo a este servlet que lo unico que hace es almacenar el Subject en la HttpSession para poder acceederlo posteriormente.
	 * @return
	 */
	private String getStoreUrl() {
		return GWT.getHostPageBaseURL() + "store";
	}
	
	private String getLogoutUrl() {
		return GWT.getHostPageBaseURL() + "logout";
	}
	
	
	@Override
	public void login(String username, String password, final Receiver<Void> rec) {
		
		String url = getAuthUrl();
		
		// envío mediante post asi pasa por el servlet de shiro y es procesado por la autentificacion.
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,url);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		StringBuilder sb = new StringBuilder();
		sb.append("username").append("=").append(URL.encode(username));
		sb.append("&");
		sb.append("password").append("=").append(URL.encode(password));
		
		try {
			builder.sendRequest(sb.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (Response.SC_OK == response.getStatusCode()) {
						
						/*
						// llamo para que se almacene en la sesion el subject.
						try {
							(new RequestBuilder(RequestBuilder.POST,getStoreUrl())).sendRequest("", new RequestCallback() {
								@Override
								public void onResponseReceived(Request request, Response response) {
									rec.onSuccess(null);
								}
								@Override
								public void onError(Request request, Throwable exception) {
									rec.onFailure(exception);
								}
							});
						} catch (RequestException e) {
							rec.onFailure(e);
						}
						*/
						rec.onSuccess(null);
						
					} else {
						
						rec.onFailure(new Exception("No se pudo autentificar correctamente al usuario"));
						
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					
					rec.onFailure(exception);
					
				}
			});
		} catch (RequestException e) {
			
			rec.onFailure(e);
			
		}
		
	}

	@Override
	public void logout(final Receiver<Void> rec) {
		
		String url = getLogoutUrl();
		
		// envío mediante post asi pasa por el servlet de shiro y es procesado por la autentificacion.
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,url);
		try {
			builder.sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (Response.SC_OK == response.getStatusCode()) {
						
						rec.onSuccess(null);
						
					} else {
						
						rec.onFailure(new Exception("No se pudo desloguear al usuario"));
						
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {
					
					rec.onFailure(exception);
					
				}
			});
		} catch (RequestException e) {
			
			rec.onFailure(e);
			
		}		
		
	}

	@Override
	public void isAuthenticated(final Receiver<Boolean> rec) {
		
		Message msg = messageUtils.method(AuthMethods.isAuthenticated);
		
		try {
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message message) {
					
					String json = message.getPayload();
					Boolean b = managerUtils.decodeBoolean(json);
					rec.onSuccess(b);
					
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
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
