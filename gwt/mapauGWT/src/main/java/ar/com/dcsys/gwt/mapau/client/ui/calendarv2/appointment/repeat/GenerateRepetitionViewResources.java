package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GenerateRepetitionViewResources extends ClientBundle {

	public static final GenerateRepetitionViewResources INSTANCE = GWT.create(GenerateRepetitionViewResources.class);
	
	@Source("GenerateRepetitionAdmin.css")
	public GenerateRepetitionViewCss style();
}
