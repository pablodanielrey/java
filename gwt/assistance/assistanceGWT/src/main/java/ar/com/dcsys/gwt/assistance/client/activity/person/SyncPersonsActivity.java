package ar.com.dcsys.gwt.assistance.client.activity.person;

import java.util.List;

import ar.com.dcsys.gwt.assistance.client.manager.PersonDataManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.client.event.MessageEvent;
import ar.com.dcsys.gwt.messages.client.event.MessageEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SyncPersonsActivity extends AbstractActivity {

	private final PersonDataManager personDataManager;
	
	@Inject
	public SyncPersonsActivity(PersonDataManager personDataManager) {
		this.personDataManager = personDataManager;
	}
	
	
	
	
	/////// notificaciones de html5 //////
	
	public native boolean supportsNotifications() /*-{
		return $wnd.webkitNotifications;
	}-*/;
	
	public native void requestNativePermission() /*-{
		if ($wnd.webkitNotifications.checkPermission() != 0) {
        	$wnd.webkitNotifications.requestPermission();
    	}
	}-*/;

	public native void  showNativeNotification(String msg) /*-{
    	if ($wnd.webkitNotifications.checkPermission() == 0) {
        	$wnd.webkitNotifications.createNotification('','Notificación', msg).show();
    	} else {
        	alert(msg);
    	}
	}-*/;
	
	////////////////////////////////////////////
	
	
	public void showNotification(String msg) {
		if (supportsNotifications()) {
			showNativeNotification(msg);
		} else {
			Window.alert(msg);
		}
	}
	

	private HandlerRegistration hr;
	private final MessageEventHandler meh = new MessageEventHandler() {
		@Override
		public void onMessage(MessageEvent event) {
			if ("persistPerson".equals(event.getType())) {
				final String json = event.getMessage();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						showNotification(json);
					}
				});
			}
		}
	};
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		if (supportsNotifications()) {
			requestNativePermission();
		}
		
		hr = eventBus.addHandler(MessageEvent.TYPE, meh);
		
		this.personDataManager.syncPersons(new Receiver<List<String>>() {
			
			@Override
			public void onSuccess(List<String> t) {
				showNotification("Personas " + t.size() + " transferidas correctamente");
			}
			
			@Override
			public void onError(String error) {
				showNotification(error);
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