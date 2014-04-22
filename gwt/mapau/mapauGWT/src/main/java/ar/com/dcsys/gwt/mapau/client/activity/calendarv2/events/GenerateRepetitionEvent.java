package ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events;

import java.util.Date;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepetitionData;

import com.google.gwt.event.shared.GwtEvent;


public class GenerateRepetitionEvent extends GwtEvent<GenerateRepetitionEventHandler> {

	public static final GwtEvent.Type<GenerateRepetitionEventHandler> TYPE = new GwtEvent.Type<GenerateRepetitionEventHandler>();
	
	
	private final Date date;
	private final Receiver<RepetitionData> data;
	
	public GenerateRepetitionEvent(Date date, Receiver<RepetitionData> data) {
		this.date = date;
		this.data = data;
	}
	
	public Date getDate() {
		return date;
	}

	public Receiver<RepetitionData> getData() {
		return data;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GenerateRepetitionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GenerateRepetitionEventHandler handler) {
		handler.onGenerateRepetition(this);
	}

	
	
}
