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

						isAuthenticated(new Receiver<Boolean>() {
							@Override
							public void onSuccess(Boolean t) {
								if (t == null || t.booleanValue() == false) {
									rec.onFailure(new Exception("No se pudo autentificar al usuario"));
								} else {
									rec.onSuccess(null);
								}
							}
							@Override
							public void onFailure(Throwable t) {
								rec.onFailure(t);
							}							
						});
						
						
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
						
						isAuthenticated(new Receiver<Boolean>() {
							@Override
							public void onSuccess(Boolean t) {
								if (t != null && t.booleanValue() == true) {
									rec.onFailure(new Exception("No se pudo desloguear al usuario"));
								} else {
									rec.onSuccess(null);
								}
							}
							@Override
							public void onFailure(Throwable t) {
								rec.onFailure(t);
							}							
						});						
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
	}

	@Override
	public void hasPermission(String perm, final Receiver<Boolean> rec) {
		
		Message msg = messageUtils.method(AuthMethods.hasPermission,perm);
		
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
			
	}

	
	
}
