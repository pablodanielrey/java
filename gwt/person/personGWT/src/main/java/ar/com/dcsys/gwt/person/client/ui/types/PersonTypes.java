package ar.com.dcsys.gwt.person.client.ui.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class PersonTypes extends Composite implements PersonTypesView {

	private static PersonTypesUiBinder uiBinder = GWT.create(PersonTypesUiBinder.class);

	interface PersonTypesUiBinder extends UiBinder<Widget, PersonTypes> {
	}

	private final List<PersonTypeEnum> typesCache = new ArrayList<PersonTypeEnum>();
	private MultiSelectionModel<PersonTypeEnum> typesSelection; 
	
	private boolean readOnly = false;
	
	@UiField FlowPanel types;
	
	public PersonTypes() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
	}

	
	@Override
	public void setReadOnly(boolean v) {
		readOnly = v;
		setTypesReadOnly(readOnly);
	}
	
	private void setTypesReadOnly(boolean t) {
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			c.setEnabled(!t);
		}
	}	
	
	/**
	 * Se encarga de mantener actualizada la interfaz contra los tipos seleccionados dentro del selectionModel.
	 */
	private final Handler typesHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			if (typesSelection == null) {
				return;
			}
			Set<PersonTypeEnum> selected = typesSelection.getSelectedSet();
			if (selected == null) {
				clear();
				return;
			}
			List<PersonTypeEnum> selectedTypes = new ArrayList<PersonTypeEnum>(selected);
			setSelectedTypes(selectedTypes);
		}
	};
	
	
	@Override
	public void setTypesSelectionModel(MultiSelectionModel<PersonTypeEnum> selection) {
		typesSelection = selection;
		typesSelection.addSelectionChangeHandler(typesHandler);
	}

	/**
	 * Limpia la selección grafica de cada widget de la vista que repesenta un tipo.
	 */
	public void clear() {
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			c.setValue(false);
		}
	}

	/**
	 * Setea a true los checkboxs que representan los tipos seleccionados.
	 * @param types
	 */
	private void setSelectedTypes(List<PersonTypeEnum> types) {
		clear();
		if (types != null && types.size() > 0) {
			for (int i = 0; i < this.types.getWidgetCount(); i++) {
				CheckBox c = (CheckBox)this.types.getWidget(i);
				c.setValue(false);
				for (PersonTypeEnum pt : types) {
					if (c.getText().equalsIgnoreCase(pt.getDescription())) {
						c.setValue(true);
					}
				}
			}
		}		
	}

	
	@Override
	public void setAllTypes(List<PersonTypeEnum> types) {
		typesCache.clear();
		typesCache.addAll(types);
		
		this.types.clear();
		
		//types = PersonTypeUtils.filter(types);
		
		for (PersonTypeEnum pt : types) {
			CheckBox c = new CheckBox(pt.getDescription());
			c.setValue(false);
			c.setEnabled(true);
			c.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					updateSelectedTypes();
				}
			});
			this.types.add(c);
		}
		
		updateSelectedTypes();
		
		setTypesReadOnly(readOnly);
	}
	
	/**
	 * Actualiza el selection model a los valores de los checkbox.
	 */
	private void updateSelectedTypes() {
		for (PersonTypeEnum pt : typesCache) {
			for (int i = 0; i < this.types.getWidgetCount(); i++) {
				CheckBox c = (CheckBox)this.types.getWidget(i);
				if (pt.getDescription().equalsIgnoreCase(c.getText())) {
					typesSelection.setSelected(pt, c.getValue());
					break;
				}
			}
		}
	}
	
	

}
