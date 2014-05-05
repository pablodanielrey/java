package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.related;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface ThisOrRelatedViewResources extends ClientBundle {

	public static final ThisOrRelatedViewResources INSTANCE = GWT.create(ThisOrRelatedViewResources.class);
	
	@Source("ThisOrRelatedAdmin.css")
	public ThisOrRelatedViewCss style();
}
