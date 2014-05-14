package ar.com.dcsys.pr.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

					List<String> ps = new ArrayList<String>();
					ps.add("1");
					ps.add("2");

					tm.test4(ps, new Receiver<String>() {
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
