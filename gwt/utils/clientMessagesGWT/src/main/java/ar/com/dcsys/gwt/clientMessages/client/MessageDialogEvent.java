package ar.com.dcsys.gwt.clientMessages.client;

import com.google.gwt.event.shared.GwtEvent;

public class MessageDialogEvent extends GwtEvent<MessageDialogEventHandler> {

	public static final Type<MessageDialogEventHandler> TYPE = new Type<MessageDialogEventHandler>();
	
	@Override
	protected void dispatch(MessageDialogEventHandler handler) {
		handler.onMessageDialog(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MessageDialogEventHandler> getAssociatedType() {
		return TYPE;
	}
	
	private boolean show = true;
	private String text = "";
	
	public MessageDialogEvent() { }
	
	public MessageDialogEvent(boolean show) { this.show = show; }
	
	public MessageDialogEvent(String text) { this.text = text; }
	
	public MessageDialogEvent(boolean show, String text) { this.show = show; this.text = text;}
	
	public String getText() { return text; }
	
	public boolean getShow() { return show; }	
}
