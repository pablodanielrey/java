package ar.com.dcsys.gwt.person.client.ui.basicData;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface PersonDataViewResources extends ClientBundle {
	
	public static final PersonDataViewResources INSTANCE = GWT.create(PersonDataViewResources.class);

	@Source("PersonDataViewAdmin.css")
	public PersonDataViewCss style();

	@Source("PersonDataViewUser.css")
	public PersonDataViewCss personDataCssUser();
	
}
