package ar.com.dcsys.gwt.person.client.ui.types;

import java.util.List;

import ar.com.dcsys.data.person.PersonType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface PersonTypesView extends IsWidget {

	public void setPresenter(Presenter presenter);

	public void clear();
	public void setAllTypes(List<PersonType> types);
	public void setTypesSelectionModel(MultiSelectionModel<PersonType> selection);
	
	public void setReadOnly(boolean v);
	
	public interface Presenter {
		
	}
	
}
