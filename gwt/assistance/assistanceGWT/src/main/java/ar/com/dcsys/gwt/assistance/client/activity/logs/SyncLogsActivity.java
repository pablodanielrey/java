package ar.com.dcsys.gwt.assistance.client.activity.logs;

import java.util.List;

import ar.com.dcsys.gwt.assistance.client.manager.LogsManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.client.event.MessageEvent;
import ar.com.dcsys.gwt.messages.client.event.MessageEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SyncLogsActivity extends AbstractActivity {

	private final LogsManager logsManager;
	
	@Inject
	public SyncLogsActivity(LogsManager logsManager) {
		this.logsManager = logsManager;
	}
	
	
	private HandlerRegistration hr;
	private final MessageEventHandler meh = new MessageEventHandler() {
		@Override
		public void onMessage(MessageEvent event) {
			if ("attLogUpdate".equals(event.getType())) {
				String json = event.getMessage();
				Window.alert(json);
			}
		}
	};
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		hr = eventBus.addHandler(MessageEvent.TYPE, meh);
		
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

	private void removeHandler() {
		try {
			if (hr != null) {
				hr.removeHandler();
			}
		} finally {
			hr = null;
		}
	}
	
	@Override
	public void onStop() {
		removeHandler();
	}

	@Override
	public void onCancel() {
		removeHandler();
	}
	
}
