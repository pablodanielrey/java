package ar.com.dcsys.gwt.person.client.ui.types;

import java.util.List;

import ar.com.dcsys.data.person.PersonTypeEnum;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface PersonTypesView extends IsWidget {

	public void setPresenter(Presenter presenter);

	public void clear();
	public void setAllTypes(List<PersonTypeEnum> types);
	public void setTypesSelectionModel(MultiSelectionModel<PersonTypeEnum> selection);
	
	public void setReadOnly(boolean v);
	
	public interface Presenter {
		
	}
	
}
