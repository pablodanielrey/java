package ar.com.dcsys.gwt.person.client.ui.common;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface PersonResources extends ClientBundle {

	@Source("fingerprint.png")
	public ImageResource fingerprint();
	
	@Source("user.png")
	public ImageResource user();
	
	@Source("clock1.png")
	public ImageResource device();
	
}
