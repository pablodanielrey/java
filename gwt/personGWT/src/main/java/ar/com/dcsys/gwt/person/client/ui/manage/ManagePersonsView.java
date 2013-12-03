package ar.com.dcsys.gwt.person.client.ui.manage;

import java.util.List;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface ManagePersonsView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();

	public void setSelectionModel(SingleSelectionModel<PersonValueProxy> person);
	public void setPersons(List<PersonValueProxy> persons);
	
	public void setAllTypes(List<PersonType> types);
	public List<PersonType> getSelectedTypes();
	public boolean isNoTypeSelected();
	
	public Panel getPersonDataPanel();
	public Panel getPersonGroupsPanel();
	
	public interface Presenter {
		public void updateUsers();
	}
	
}
