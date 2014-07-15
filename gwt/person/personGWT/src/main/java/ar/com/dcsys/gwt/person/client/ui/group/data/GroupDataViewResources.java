package ar.com.dcsys.gwt.person.client.ui.group.data;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GroupDataViewResources extends ClientBundle {
	
	public static final GroupDataViewResources  INSTANCE = GWT.create(GroupDataViewResources.class);

	@Source("GroupDataViewAdmin.css")
	public GroupDataViewCss style();

	@Source("GroupDataViewUser.css")
	public GroupDataViewCss groupDataCssUser();

}
