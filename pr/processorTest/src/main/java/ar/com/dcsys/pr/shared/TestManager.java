package ar.com.dcsys.pr.shared;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface TestManager {

	public void test1(String p1, Long p2, Receiver<String> rec);
	
}
