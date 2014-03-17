package ar.com.dcsys.gwt.assistance.client.ui.common;


/**
 * Datos de los recursos 
 * 
 * 
 * relojes
 * 
 * http://openclipart.org/detail/90235/clock-michael-breuer-04-by-anonymous
 * http://openclipart.org/detail/145873/a-blue-and-chrome-clock-by-jhnri4
 * http://openclipart.org/detail/101011/eci-clock.svg-by-dpkpm007
 * http://openclipart.org/detail/12597/analog-clock-by-anonymous-12597
 * 
 * usuarios
 * 
 * http://openclipart.org/detail/11055/users-by-sampler-11055
 * 
 * 
 * 
 */

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AssistanceResources extends ClientBundle {

	@Source("fingerprint.png")
	public ImageResource fingerprint();
	
	@Source("clock1.png")
	public ImageResource device();
	
}
