package ar.com.dcsys.gwt.person.client.ui.group.persons;

import ar.com.dcsys.data.group.Group;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupPersonsView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public Panel getMembersPanel();
	public Panel getOutPersonsPanel();
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Group> selection);
		public void add();
		public void remove();
	}
	
}
