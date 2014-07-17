package ar.com.dcsys.gwt.person.client.ui.group.persons.members;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GroupMemberViewResources extends ClientBundle {
	
	public static final GroupMemberViewResources  INSTANCE = GWT.create(GroupMemberViewResources.class);

	@Source("GroupMemberViewAdmin.css")
	public GroupMemberViewCss style();

	@Source("GroupMemberViewUser.css")
	public GroupMemberViewCss groupMemberCssUser();

}
