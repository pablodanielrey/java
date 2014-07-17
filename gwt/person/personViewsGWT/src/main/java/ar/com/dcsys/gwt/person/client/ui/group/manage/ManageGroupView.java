package ar.com.dcsys.gwt.person.client.ui.group.manage;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public interface ManageGroupView extends IsWidget{

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public Panel getGroupListPanel();
	public Panel getPersonsPanel();
	public Panel getGroupDataPanel();
	
	public void closeGroupDataPanel();
	
	public void changeTextCreateButton(String text);
	
	public interface Presenter {
		public void persist();
	}
	
}
