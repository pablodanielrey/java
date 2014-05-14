package ar.com.dcsys.pr.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.pr.shared.TestManager;

public class TestManagerBean implements TestManager {

	private static Logger logger = Logger.getLogger(TestManagerBean.class.getName());
	
	
	@Override
	public void test3(String pepe, Receiver<String> rec) {
		
		logger.log(Level.SEVERE, "mensaje : " + pepe);
		logger.log(Level.SEVERE, "respondiendo : respuesta");
		
		rec.onSuccess("respuesta");
	}

	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}


	

}
