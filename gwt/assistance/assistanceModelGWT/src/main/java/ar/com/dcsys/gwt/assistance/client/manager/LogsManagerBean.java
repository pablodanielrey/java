package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.List;

import ar.com.dcsys.gwt.assistance.shared.LogsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;

public class LogsManagerBean implements LogsManager {

	
	private final LogsManagerTransfer logsManagerTransfer = GWT.create(LogsManagerTransfer.class);
	
	private final WebSocket ws;
	
	@Inject
	public LogsManagerBean(WebSocket ws) {
		this.ws = ws;
		logsManagerTransfer.setTransport(ws);
	}
	
	@Override
	public void syncLogs(Receiver<List<String>> logs) {
		logsManagerTransfer.syncLogs(logs);
	}

}
