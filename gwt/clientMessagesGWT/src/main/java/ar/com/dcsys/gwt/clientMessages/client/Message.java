package ar.com.dcsys.gwt.clientMessages.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class Message extends Composite {

	private static MessageUiBinder uiBinder = GWT.create(MessageUiBinder.class);

	interface MessageUiBinder extends UiBinder<Widget, Message> {
	}

	@UiField DialogBox dialog;
	@UiField Label text;
	@UiField Button close;
	
	private Timer timer;
	
	
	@Inject
	public Message(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		dialog.setGlassEnabled(true);
		dialog.setModal(true);
		dialog.setAnimationEnabled(true);
		
		eventBus.addHandler(MessageDialogEvent.TYPE, new MessageDialogEventHandler(){
			@Override
			public void onMessageDialog(MessageDialogEvent event) {
				if (event.getShow()) {
					
					text.setText(event.getText());
					
					if (timer != null) {
						timer.cancel();
					}
					timer = new Timer() {
						private int count = 0;
						
						@Override
						public void run() {
							if (count == 15) {
								text.setText("");
								dialog.hide();								
							}
							count = count + 1;
						}
					};
					timer.scheduleRepeating(1000);					
					
					dialog.center();
					dialog.show();
				} else {
					if (timer != null) {
						timer.cancel();
					}
					timer = null;
					
					text.setText("");
					dialog.hide();
				}
			}
		});
	}
	
	@UiHandler("close")
	public void onClose(ClickEvent event) {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;		
		text.setText("");
		dialog.hide();		
	}

}
