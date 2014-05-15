package ar.com.dcsys.pr.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.Telephone;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;
import ar.com.dcsys.pr.client.gin.Injector;
import ar.com.dcsys.pr.shared.TestManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;

public class ProcessorTest implements EntryPoint {

	private final static Logger logger = Logger.getLogger(ProcessorTest.class.getName());
	
	/*
   	private interface GTestManager extends GwtClientManager<TestManager> {
 
 		
 	}
 	*/
	
	private final Injector injector = GWT.create(Injector.class);
	
	private class PersonB implements Person {

		public PersonB(String dni) {
			this.dni = dni;
		}
		
		private String dni;
		
		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setId(String id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setName(String n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getLastName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLastName(String n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getDni() {
			// TODO Auto-generated method stub
			return dni;
		}

		@Override
		public void setDni(String dni) {
			this.dni = dni;
		}

		@Override
		public String getAddress() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setAddress(String addr) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCity() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCity(String city) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCountry() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCountry(String country) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Gender getGender() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setGender(Gender gender) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Date getBirthDate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setBirthDate(Date d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<PersonType> getTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTypes(List<PersonType> types) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Telephone> getTelephones() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTelephones(List<Telephone> tels) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public void onModuleLoad() {
		
		final WebSocket ws = injector.getWebSocket();
	
		EventBus bus = injector.getEventBus();

		
		try {
			final TestManager tm = GWT.create(TestManager.class);
			tm.setTransport(ws);

			
			bus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
				@Override
				public void onOpen() {
					logger.log(Level.INFO,"socket abierto");

					List<Person> ps = new ArrayList<Person>();
					ps.add(new PersonB("1"));
					ps.add(new PersonB("2"));

					tm.test5(ps, new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							Window.alert(t);
							try {
								ws.close();
							} catch (Exception e) {
								logger.log(Level.SEVERE,e.getMessage());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
							try {
								ws.close();
							} catch (Exception e) {
								logger.log(Level.SEVERE,e.getMessage());
							}
						}
					});
				
				}
				
				@Override
				public void onClose() {
					logger.log(Level.INFO,"socket cerrado");
				}
			});
			
			
			ws.open();
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
	}

}
