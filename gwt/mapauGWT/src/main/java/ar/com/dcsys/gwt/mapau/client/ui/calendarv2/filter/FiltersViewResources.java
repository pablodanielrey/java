package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.filter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface FiltersViewResources extends ClientBundle {
	
	public static final FiltersViewResources  INSTANCE = GWT.create(FiltersViewResources.class);

	@Source("FiltersAdmin.css")
	public FiltersViewCss style();

}
