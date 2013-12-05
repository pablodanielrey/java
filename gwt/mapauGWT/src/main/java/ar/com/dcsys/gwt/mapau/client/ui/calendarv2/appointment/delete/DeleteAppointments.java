package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.delete;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.DeleteAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.DeleteAppointmentsView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DeleteAppointments extends Composite implements DeleteAppointmentsView {

	private static DeleteAppointmentsUiBinder uiBinder = GWT.create(DeleteAppointmentsUiBinder.class);

	interface DeleteAppointmentsUiBinder extends UiBinder<Widget, DeleteAppointments> {
	}
	
	@UiField DeleteAppointmentsViewResources res;
	
	@UiField Label onlyOne;
	@UiField Label relatedAlso;
	@UiField Label cancel;
	@UiField(provided=true) Label message;
	
	private DeleteAppointmentsView.Presenter presenter;

	public DeleteAppointments() {
		createMessage();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void createMessage() {
		message = new Label();
		String msg = "¿Está seguro que desea eliminar la Reserva?";
		setMessage(msg);
	}
	
	private void setMessage(String msg) {
		this.message.setText(msg);
	}

	@Override
	public void setAppointment(MapauAppointment a) {
		
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	@UiHandler("onlyOne")
	public void onOnlyOne(ClickEvent event) {
		presenter.onlyOne();
	}
	
	@UiHandler("relatedAlso")
	public void onRelatedAlso(ClickEvent event) {
		presenter.relatedAlso();
	}
	
	@UiHandler("cancel")
	public void onCancel(ClickEvent event) {
		presenter.cancel();
	}

}
