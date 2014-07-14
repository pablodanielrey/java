package ar.com.dcsys.gwt.menu.client.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface MenuViewResources extends ClientBundle {

	public static final MenuViewResources INSTANCE = GWT.create(MenuViewResources.class);
	
	@Source("MenuView.css")
	public MenuViewCss style();
}
