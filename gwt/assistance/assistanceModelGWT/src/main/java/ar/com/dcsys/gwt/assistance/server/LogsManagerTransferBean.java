package ar.com.dcsys.gwt.assistance.server;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.gwt.assistance.shared.LogsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.model.device.DevicesManager;

public class LogsManagerTransferBean implements LogsManagerTransfer {

	@Override
	public void setTransport(Transport t) {
		
	}


	private final DevicesManager devicesManager;
	
	@Inject
	public LogsManagerTransferBean(DevicesManager devicesManager) {
		this.devicesManager = devicesManager;
	}
	
	@Override
	public void syncLogs(Receiver<List<String>> logs) {
		
		
		List<String> ids;
		try {
			ids = devicesManager.syncLogs();
			logs.onSuccess(ids);
			
		} catch (AttLogException | DeviceException e) {
			logs.onError(e.getMessage());
			
		}

		
	}

}
