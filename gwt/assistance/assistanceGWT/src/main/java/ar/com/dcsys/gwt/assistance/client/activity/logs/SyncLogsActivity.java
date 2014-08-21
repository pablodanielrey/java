package ar.com.dcsys.gwt.assistance.client.activity.logs;

import java.util.List;

import ar.com.dcsys.gwt.assistance.client.manager.LogsManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class SyncLogsActivity extends AbstractActivity {

	private final LogsManager logsManager;
	
	@Inject
	public SyncLogsActivity(LogsManager logsManager) {
		this.logsManager = logsManager;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		logsManager.syncLogs(new Receiver<List<String>>() {
			
			@Override
			public void onSuccess(List<String> t) {
				Window.alert("Logs " + t.size() + " transferidos correctamente");
			}
			
			@Override
			public void onError(String error) {
				Window.alert(error);
			}
		});
		
	}
	
}
