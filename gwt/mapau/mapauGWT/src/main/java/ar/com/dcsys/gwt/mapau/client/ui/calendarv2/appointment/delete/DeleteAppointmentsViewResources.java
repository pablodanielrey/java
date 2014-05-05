package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.delete;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface DeleteAppointmentsViewResources extends ClientBundle {
	
	public static final DeleteAppointmentsViewResources  INSTANCE = GWT.create(DeleteAppointmentsViewResources.class);

	@Source("DeleteAppointmentsAdmin.css")
	public DeleteAppointmentsViewCss style();

}
