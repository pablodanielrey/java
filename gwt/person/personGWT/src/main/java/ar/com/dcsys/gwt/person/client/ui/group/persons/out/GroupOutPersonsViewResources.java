package ar.com.dcsys.gwt.person.client.ui.group.persons.out;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GroupOutPersonsViewResources extends ClientBundle {
	
	public static final GroupOutPersonsViewResources  INSTANCE = GWT.create(GroupOutPersonsViewResources.class);

	@Source("GroupOutPersonsViewAdmin.css")
	public GroupOutPersonsViewCss style();

	@Source("GroupOutPersonsViewUser.css")
	public GroupOutPersonsViewCss groupOutPersonsCssUser();

}
