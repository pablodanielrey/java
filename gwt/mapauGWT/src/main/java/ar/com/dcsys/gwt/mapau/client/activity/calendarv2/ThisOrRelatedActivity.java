package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ThisOrRelatedEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ThisOrRelatedEventHandler;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ThisOrRelatedView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.related.ThisOrRelatedViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.related.ThisOrRelatedViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class ThisOrRelatedActivity extends AbstractActivity implements ThisOrRelatedView.Presenter {

	private final ThisOrRelatedView view;
	private ResettableEventBus eventBus;
	
	private Receiver<Boolean> receiver;
	
	public ThisOrRelatedActivity(ThisOrRelatedView view) {
		this.view = view;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		this.eventBus = new ResettableEventBus(eventBus);
		
		this.eventBus.addHandler(ThisOrRelatedEvent.TYPE, new ThisOrRelatedEventHandler() {
			
			@Override
			public void onThisOrRelated(ThisOrRelatedEvent event) {
				
				receiver = event.getReceiver();
				view.clear();
				
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						ThisOrRelatedActivity.this.eventBus.fireEvent(new ShowViewEvent(ThisOrRelatedView.class.getName()));
					}
				});
			}
		});
		
		view.setPresenter(this);
		
		ThisOrRelatedViewCss style = ThisOrRelatedViewResources.INSTANCE.style();
		style.ensureInjected();
		
		panel.setWidget(view);
		
	}
	
	@Override
	public void onStop() {
		view.clear();
		
		this.eventBus.removeHandlers();
		this.eventBus = null;
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}	
	
	@Override
	public void commit(boolean v) {
		if (receiver == null) {
			showMessage("receiver == null");
			return;
		}
		
		eventBus.fireEvent(new HideViewEvent(ThisOrRelatedView.class.getName()));
		receiver.onSuccess(v);
		
		receiver = null;
	}

}
