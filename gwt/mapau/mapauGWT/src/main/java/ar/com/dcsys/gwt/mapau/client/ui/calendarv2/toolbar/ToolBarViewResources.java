package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.toolbar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface ToolBarViewResources extends ClientBundle {
	
	public static final ToolBarViewResources  INSTANCE = GWT.create(ToolBarViewResources.class);

	@Source("ToolBarAdmin.css")
	public ToolBarViewCss style();

}
