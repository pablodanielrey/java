package ar.com.dcsys.gwt.person.client.ui.widget.selectionPersonList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.person.client.ui.widget.SelectionPersonListWidget;
import ar.com.dcsys.gwt.person.client.ui.widget.personList.PersonList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;


public class SelectionPersonList extends Composite implements SelectionPersonListWidget {

	private static SelectionPersonListUiBinder uiBinder = GWT.create(SelectionPersonListUiBinder.class);

	interface SelectionPersonListUiBinder extends UiBinder<Widget, SelectionPersonList> {	}

	private final List<Person> acceptableValues = new ArrayList<Person>();
	private final List<Person> selectedPersons = new ArrayList<Person>();
	private MultiSelectionModel<Person> selection;
	
	@UiField(provided=true) PersonList personList;
	@UiField(provided=true) ValueListBox<Person> person;
	@UiField Button add;
	
	private void createPerson() {
		person = new ValueListBox<Person>(new Renderer<Person>() {
			private String getValue(Person p) {
				return p.getLastName() + " " + p.getName();
			}
			
			@Override
			public String render(Person object) {
				if (object == null) {
					return "";
				}
				return getValue(object);
			}
			@Override
			public void render(Person object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
	}
	
	@UiHandler("add")
	public void onAdd(ClickEvent event) {
		Person pp = person.getValue();
		
		if (pp == null) {
			return;
		}
		
		acceptableValues.remove(pp);
		if (acceptableValues.size() <= 0) {
			person.setValue(null);
		} else {
			person.setValue(acceptableValues.get(0));
		}
		person.setAcceptableValues(acceptableValues);
		
		if (selection != null) {
			selection.setSelected(pp, true);
		}
		selectedPersons.add(pp);
		personList.setPersons(selectedPersons);
	}
	
	public SelectionPersonList() {
		personList = new PersonList();
		createPerson();
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPersons(List<Person> persons) {
		acceptableValues.clear();
		if (persons != null) {
			acceptableValues.addAll(persons);
		}
		if (acceptableValues.size() > 0) {
			person.setValue(acceptableValues.get(0));
		}
		person.setAcceptableValues(acceptableValues);
		
		if (selection != null) {
			selection.clear();
		}
		selectedPersons.clear();
		personList.clear();
	}

	@Override
	public void clear() {
		setReadOnly(false);
		if (selection != null) {
			selection.clear();
		}
		selectedPersons.clear();
		personList.clear();

		acceptableValues.clear();
		person.setValue(null);
		person.setAcceptableValues(acceptableValues);
	}

	@Override
	public void setSelectionModel(SelectionModel<Person> selection) {
		personList.setSelectionModel(selection);
	}

	@Override
	public void setListSelectionModel(MultiSelectionModel<Person> selection) {
		this.selection = selection;
	}
	
	@Override
	public void setReadOnly(boolean v) {
		add.setEnabled(!v);
		setEnabled(person,!v);
	}
	
	/**
	 * Segun el valor del parametro enabled, habilita o no el listBox
	 * @param listBox
	 * @param enabled
	 */
	public void setEnabled(ValueListBox<?> listBox, final boolean enabled) {
		if (enabled) {
			DOM.removeElementAttribute(listBox.getElement(), "disabled");
		} else {
			DOM.setElementAttribute(listBox.getElement(), "disabled", "disabled");
		}
	}


}
