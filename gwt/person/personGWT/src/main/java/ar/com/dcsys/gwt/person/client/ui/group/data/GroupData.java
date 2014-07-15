package ar.com.dcsys.gwt.person.client.ui.group.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.gwt.person.client.common.EnumGroupTypesSort;
import ar.com.dcsys.utils.GroupSort;
import ar.com.dcsys.utils.GroupTypeUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class GroupData extends Composite implements GroupDataView {

	private static GroupDataUiBinder uiBinder = GWT.create(GroupDataUiBinder.class);
	
	@UiField GroupDataViewResources res;

	interface GroupDataUiBinder extends UiBinder<Widget, GroupData> {
	}
	
	@UiField TextBox name;
	@UiField TextBox mail;
	@UiField(provided = true) ValueListBox<Group> groupParent;
	@UiField FlowPanel types;
	

	private final List<GroupType> groupTypesCache = new ArrayList<GroupType>();
	
	private final SingleSelectionModel<Group> parentSelection;
	
	private GroupDataView.Presenter presenter;

	
	private void createParent() {
		groupParent = new ValueListBox<Group>(new Renderer<Group>() {
			private String getValue(Group g) {
				if (g == null) {
					return "No posee";
				}
				return g.getName();
			}
			
			@Override
			public String render(Group object) {
				return getValue(object);
			}
			
			@Override
			public void render(Group object, Appendable appendable) throws IOException {
				appendable.append(getValue(object));
			}
		});
		groupParent.addValueChangeHandler(new ValueChangeHandler<Group>() {
			@Override
			public void onValueChange(ValueChangeEvent<Group> event) {
				if (parentSelection != null) {
					Group g = groupParent.getValue();
					parentSelection.setSelected(g, true);
				}
			}
		});		
	}	
	
	public GroupData() {
		createParent();
		initWidget(uiBinder.createAndBindUi(this));
		parentSelection = new SingleSelectionModel<Group>();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void clear() {
		clearData();
		groupTypesCache.clear();
		groupParent.setAcceptableValues(new ArrayList<Group>());
	}
	
	@Override
	public void clearData() {
		clearParent();
		name.setText("");
		mail.setText("");
		clearGroupTypes();		
	}
	
	private void clearGroupTypes() {
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			c.setValue(false);
		}
	}	
	
	private void clearParent() {
		groupParent.setValue(null);	
	}
	
	@Override
	public String getMail() {
		return mail.getText();
	}
	
	@Override
	public String getName() {
		return name.getText();
	}
	
	@Override
	public List<GroupType> getSelectedGroupTypes() {
		List<GroupType> selected = new ArrayList<GroupType>();
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			if (c.getValue()) {
				for (GroupType pt : groupTypesCache) {
					if (GroupTypeUtils.getDescription(pt).equalsIgnoreCase(c.getText())) {
						selected.add(pt);
					}
				}
			}
		}
		return selected;
	}
	
	/**
	 * Setea los datos del grupo dentro de la ventana de edición.
	 * No setea el padre.
	 */
	@Override
	public void setGroup(Group group) {
		name.setText(group.getName());
		
		String mail = findPrimaryMail(group); 
		if (mail != null) {
			this.mail.setText(mail);
		}
	}
	
	/**
	 * Busca el mail primario del grupo en caso de existir mas de 1 mail.
	 * @param g
	 * @return
	 */
	private String findPrimaryMail(Group g) {
		List<Mail> mails = g.getMails();
		if (mails == null) {
			return null;
		}
		for (Mail m : mails) {
			if (m.isPrimary()) {
				return m.getMail();
			}
		}
		return null;
	}	
	
	@Override
	public void setGroupTypes(List<GroupType> types) {
		
		groupTypesCache.clear();
		
		if (types == null) {
			return;
		}

		EnumGroupTypesSort.sort(types);
		groupTypesCache.addAll(types);
				
		this.types.clear();
		for (GroupType pt : types) {
			CheckBox c = new CheckBox(GroupTypeUtils.getDescription(pt));
			c.setValue(false);
			this.types.add(c);
		}
	}
	
	@Override
	public void setParents(List<Group> groups) {
		groupParent.setValue(null);		
		groups.add(null);
		GroupSort.sort(groups);
		groupParent.setAcceptableValues(groups);
		if (parentSelection != null) {
			parentSelection.setSelected(null,true);
		}		
	}
	
	@Override
	public void setParent(Group group) {
		groupParent.setValue(group);
	}
	
	
	@Override
	public Group getGroupParent() {
		return parentSelection.getSelectedObject();
	}
	
	@Override
	public void setSelectedGroupTypes(List<GroupType> types) {
		if (types != null && types.size() > 0) {
			for (int i = 0; i < this.types.getWidgetCount(); i++) {
				CheckBox c = (CheckBox)this.types.getWidget(i);
				c.setValue(false);
				for (GroupType pt : types) {
					if (c.getText().equalsIgnoreCase(GroupTypeUtils.getDescription(pt))) {
						c.setValue(true);
					}
				}
			}
		}
	}	
	

}
