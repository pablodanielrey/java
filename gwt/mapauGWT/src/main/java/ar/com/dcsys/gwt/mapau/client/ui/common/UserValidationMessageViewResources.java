package ar.com.dcsys.gwt.mapau.client.ui.common;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface UserValidationMessageViewResources extends ClientBundle {

	public static final UserValidationMessageViewResources INSTANCE = GWT.create(UserValidationMessageViewResources.class);
	
	@Source("UserValidationMessageAdmin.css")
	public UserValidationMessageViewCss style();
}
