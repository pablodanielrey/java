package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.List;

import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface LogsManager {

	public void syncLogs(Receiver<List<String>> logs);
	
}
