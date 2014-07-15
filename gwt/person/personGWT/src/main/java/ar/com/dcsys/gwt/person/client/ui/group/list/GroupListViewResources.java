package ar.com.dcsys.gwt.person.client.ui.group.list;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GroupListViewResources extends ClientBundle {
	
	public static final GroupListViewResources  INSTANCE = GWT.create(GroupListViewResources.class);

	@Source("GroupListViewAdmin.css")
	public GroupListViewCss style();

	@Source("GroupListViewUser.css")
	public GroupListViewCss groupListCssUser();	

}
