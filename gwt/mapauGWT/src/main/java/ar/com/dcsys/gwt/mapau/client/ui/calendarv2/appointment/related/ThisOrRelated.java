package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.related;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ThisOrRelatedView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ThisOrRelatedView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ThisOrRelated extends Composite implements ThisOrRelatedView {

	private static ThisOrRelatedUiBinder uiBinder = GWT.create(ThisOrRelatedUiBinder.class);

	interface ThisOrRelatedUiBinder extends UiBinder<Widget, ThisOrRelated> {
	}

	public ThisOrRelated() {
		createActions();
		createMessage();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
/* ---------------------------------------------------------------------------------------------- *
* ----------------------------------- GENERALS -------------------------------------------------- *
* ---------------------------------------------------------------------------------------------- */	

	
	@UiField ThisOrRelatedViewResources res;
	@UiField(provided=true) Label actionThis;
	@UiField(provided=true) Label actionRelated;
	
	private ThisOrRelatedView.Presenter presenter;
	
	private void createActions() {
		actionThis = new Label();
		actionThis.setText("Sólo ésta");
		actionThis.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				presenter.commit(false);
			}
		});
		
		actionRelated = new Label();
		actionRelated.setText("Ésta y las Relacionadas");
		actionRelated.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (presenter == null) {
					return;
				}
				presenter.commit(true);
			}
		});
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
/* ---------------------------------------------------------------------------------------------- *
* ----------------------------------- MENSAJE --------------------------------------------------- *
* ---------------------------------------------------------------------------------------------- */	
	
	
	@UiField(provided=true) Label message;
	
	private void createMessage() {
		message = new Label();
		message.setText("¿Desea modificar esta reserva y las relacionadas o sólo ésta?");
	}

}
