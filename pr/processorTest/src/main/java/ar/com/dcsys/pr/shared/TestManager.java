package ar.com.dcsys.pr.shared;

import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface TestManager extends Manager {

	public void test3(String pepe, Receiver<String> rec);
	
}