package ar.com.dcsys.pr.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.GwtClientManager;
import ar.com.dcsys.pr.shared.TestManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class ProcessorTest implements EntryPoint {

	private final static Logger logger = Logger.getLogger(ProcessorTest.class.getName());
	
   	private interface GTestManager extends GwtClientManager<TestManager> {
 
 		
 	}
	
	@Override
	public void onModuleLoad() {
		
		try {
			TestManager tm = GWT.create(GTestManager.class);
			
			tm.test3("pepe", new Receiver<String>() {
				@Override
				public void onSuccess(String t) {
					Window.alert(t);
				}
				
				@Override
				public void onError(String error) {
					Window.alert(error);
				}
			});
			
			Window.alert("Hola Mundoooo");
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

}
