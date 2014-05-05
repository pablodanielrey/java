package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface CreateModifyAppointmentsViewResources extends ClientBundle {
	
	public static final CreateModifyAppointmentsViewResources  INSTANCE = GWT.create(CreateModifyAppointmentsViewResources.class);

	@Source("CreateModifyAppointmentsAdmin.css")
	public CreateModifyAppointmentsViewCss style();

}
