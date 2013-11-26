package ar.com.dcsys.gwt.person.client.manager;

import ar.com.dcsys.gwt.person.PersonProxy;
import ar.com.dcsys.gwt.person.client.ws.WebsocketState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class PersonsManager {

	interface PersonFactory extends AutoBeanFactory {
		AutoBean<PersonProxy> person();
	}
	
	private final PersonFactory personFactory;
		
	private final String url;
	private final WebsocketListener wsListener;
	private final Websocket socket;
	private WebsocketState wsState;
	
	
	public PersonsManager() {
		personFactory = GWT.create(PersonFactory.class);
		
		String host = GWT.getHostPageBaseURL();
		int from = 0;
		if (host.toLowerCase().startsWith("http://")) {
			from = 5;
		} else if (host.toLowerCase().startsWith("https://")) {
			from = 6;
		}
		url = "ws://" + host.substring(from) + "websockets";
		
		wsListener = new WebsocketListener() {
			@Override
			public void onClose() {
				Window.alert("Se cerro el websocket");
				wsState = WebsocketState.CLOSED;
			}
			@Override
			public void onMessage(String msg) {
				Window.alert("Llego el siguiente mensaje : " + msg);
			}
			@Override
			public void onOpen() {
				Window.alert("Se abrio el websocket");
				wsState = WebsocketState.OPEN;
			}
		};
		
		socket = new Websocket(url);
		socket.addListener(wsListener);
		wsState = WebsocketState.CLOSED;
	}
	
	
	private String serializeToJson(PersonProxy person) {
		AutoBean<PersonProxy> bean = AutoBeanUtils.getAutoBean(person);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	private PersonProxy deserializeFromJson(String json) {
		AutoBean<PersonProxy> bean = AutoBeanCodex.decode(personFactory, PersonProxy.class, json);
		PersonProxy person = bean.as();
		return person;
	}
	
	
	public PersonProxy getPerson() {
		AutoBean<PersonProxy> bean = personFactory.create(PersonProxy.class); 
		PersonProxy person = bean.as();
		return person;
	}
	
	
	public void persist(PersonProxy person) {
		if (wsState == WebsocketState.CLOSED) {
			socket.open();
		}
		String json = serializeToJson(person);
		socket.send(json);
	}
	
}
