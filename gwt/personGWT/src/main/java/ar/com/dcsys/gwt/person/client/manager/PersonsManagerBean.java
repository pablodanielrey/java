package ar.com.dcsys.gwt.person.client.manager;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.person.shared.PersonProxy;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class PersonsManagerBean implements PersonsManager {

	interface PersonFactory extends AutoBeanFactory {
		AutoBean<PersonProxy> person();
	}
	
	private final MessageFactory messageFactory;
	private final PersonFactory personFactory;
	private final WebSocket socket;
	
	
	@Inject
	public PersonsManagerBean(WebSocket ws) {
		messageFactory = GWT.create(MessageFactory.class);
		personFactory = GWT.create(PersonFactory.class);
		socket = ws;
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
	
	
	public void persist(PersonProxy person, Receiver<String> receiver) {
		try {
			String json = serializeToJson(person);
			
			Message msg = messageFactory.message().as(); 
			msg.setType(MessageType.FUNCTION);
			msg.setPayload(json);
			
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message message) {
					Window.alert("Mensaje : " + message.getPayload());
				}
				@Override
				public void onFailure(Throwable t) {
					Window.alert("Error!!");
				}
			});
			
		} catch (Exception e) {
			receiver.onFailure(e);
		}
	}
	
}
