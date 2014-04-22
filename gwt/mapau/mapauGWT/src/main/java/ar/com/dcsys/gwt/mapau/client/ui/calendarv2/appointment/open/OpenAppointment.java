package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.open;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView.Operation;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OpenAppointment extends Composite implements OpenAppointmentView {

	private static OpenAppointmentUiBinder uiBinder = GWT.create(OpenAppointmentUiBinder.class);

	interface OpenAppointmentUiBinder extends UiBinder<Widget, OpenAppointment> {
	}

	@UiField OpenAppointmentViewResources res;
	
	@UiField Label title;
	@UiField Label date;
	@UiField FlowPanel actionsPanel;
	
	private OpenAppointmentView.Presenter presenter;
	
	private final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		
	
	private class OperationsLink {
		private Operation operation;
		private String label;
		
		public OperationsLink(Operation operation, String label) {
			this.operation = operation;
			this.label = label;
		}
	}
	
	private final OperationsLink[] operations = new OperationsLink[]{ 
		new OperationsLink(Operation.ASSIGN,"Asignar Aula"),
		new OperationsLink(Operation.DELETE, "Eliminar"),
		new OperationsLink(Operation.EDIT, "Editar"),
		new OperationsLink(Operation.REPEAT, "Generar repetición")
	};
	
	
	
	public OpenAppointment() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@Override
	public void clear() {
		clearData();
	}
	
	private void clearData() {
		this.title.setText("");
		this.date.setText("");
		this.actionsPanel.clear();
	}
	
	@Override
	public void setAppointment(MapauAppointment app) {

		if (app == null) {
			return;
		}
		String title = app.getTitle();
		
		this.title.setText(title);
		
		Date date = app.getStart();
		if (date == null) {
			return;
		}
		String dateStr = dateF.format(date);
		@SuppressWarnings("deprecation")
		String startStr = date.getHours() +  ":" + date.getMinutes(); 
		
		Date end = app.getEnd();
		if (end == null) {
			return;
		}
		@SuppressWarnings("deprecation")
		String endStr = end.getHours() +  ":" + end.getMinutes();
		
		this.date.setText(dateStr + ", " + startStr + " - " + endStr);
	}	
	
	@Override
	public void setOperations(List<Operation> operations) {
		this.actionsPanel.clear();
		if (operations == null || operations.size() <= 0) {
			return;
		}
		createOperations(operations);
	}
	
	private void createOperations(List<Operation> permittedOperations) {

		int count = 0;
		for (OperationsLink ol : operations) {

			final Operation operation = ol.operation;
			if (!permittedOperations.contains(operation)) {
				continue;
			}
			
			Label item = createItem(ol.label);
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.startOperation(operation);
				}
			});
			if (count > 0) {
				createSeparator();
			}
			this.actionsPanel.add(item);
			count ++;		
		}
		
	}
	
	private Label createItem(String name) {
		Label item = new Label();
		item.setText(name);
		item.setStyleName(res.style().actionItem());
		return item;
	}
	
	private void createSeparator() {
		FlowPanel separator = new FlowPanel();
		separator.setStyleName(res.style().separator());
		this.actionsPanel.add(separator);
	}
	

}
