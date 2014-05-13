package ar.com.dcsys.pr.client;

import ar.com.dcsys.pr.GwtClientManager;
import ar.com.dcsys.pr.shared.TestManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

public class ProcessorTest implements EntryPoint {

	
 	private interface GTestManager extends GwtClientManager<TestManager> {
 		
 	}
	
	@Override
	public void onModuleLoad() {
		
		GTestManager tm = GWT.create(GTestManager.class);
		
		Window.alert("Hola Mundoooo");
	}

}
