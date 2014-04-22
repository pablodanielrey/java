package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.assign;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface AssignClassroomViewResources extends ClientBundle {
	
	public static final AssignClassroomViewResources INSTANCE = GWT.create(AssignClassroomViewResources.class);
	
	@Source("AssignClassroomAdmin.css")
	public AssignClassroomViewCss style();

}
