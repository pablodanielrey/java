package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.open;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface OpenAppointmentViewResources extends ClientBundle {
	
	public static final OpenAppointmentViewResources  INSTANCE = GWT.create(OpenAppointmentViewResources.class);

	@Source("OpenAppointmentAdmin.css")
	public OpenAppointmentViewCss style();

}
