package ar.com.dcsys.pr.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
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

	@Override
	public void test1(Person person, Receiver<String> rec) {

		logger.log(Level.SEVERE, "mensaje : " + person.toString());
		
		rec.onSuccess(person.getDni());
		
	}

	@Override
	public void test4(List<String> pepe, Receiver<String> rec) {
		logger.log(Level.SEVERE, "mensaje : " + pepe.size());
		
		rec.onSuccess(pepe.get(pepe.size() - 1));
	}


	

}
