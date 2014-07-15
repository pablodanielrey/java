package ar.com.dcsys.gwt.person.client.ui.group.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.person.client.common.EnumGroupTypesSort;
import ar.com.dcsys.gwt.person.client.common.filter.FilterGroup;
import ar.com.dcsys.gwt.person.client.common.filter.FilterGroupName;
import ar.com.dcsys.utils.GroupSort;
import ar.com.dcsys.utils.GroupTypeUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class GroupList extends Composite implements GroupListView {

	private static GroupListUiBinder uiBinder = GWT.create(GroupListUiBinder.class);

	interface GroupListUiBinder extends UiBinder<Widget, GroupList> {
	}
	
	
	private final FilterGroup[] filtersGroups = new FilterGroup[] {new FilterGroupName()};	
	
	private final List<Group> groupsData;
	private final List<Group> groupsFilteredData;	
	private Timer filterTimerGroups = null;
	
	private final List<GroupType> groupTypesCache = new ArrayList<GroupType>();
	
	private SingleSelectionModel<GroupType> groupTypesSelection;
	
	@UiField(provided=true) TextBox filterGroup;
	@UiField(provided=true) Label groupsCount;
	
	@UiField(provided=true) ValueListBox<GroupType> groupTypes;
	
	@UiField(provided=true) DataGrid<Group> groups;
	
	
	private GroupListView.Presenter presenter;

	private void createFilterGroup() {
		
		groupsCount = new Label("0");
		
		filterGroup = new TextBox();
		filterGroup.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimerGroups != null) {
					filterTimerGroups.cancel();
				}
				filterTimerGroups = new Timer() {
					public void run() {
						filterTimerGroups = null;
						filterGroups();						
					};
				};
				filterTimerGroups.schedule(2000);				
			}
		});
	}		
	
	private void createGroups() {
		
		TextColumn<Group> name = new TextColumn<Group>() {
			public String getValue(Group object) {
				if (object == null) {
					return "";
				}
				return object.getName();
			};
		};
		groups = new DataGrid<Group>();
		groups.addColumn(name,"Grupo");
	}
	
	private void createGroupTypes() {		
		groupTypes = new ValueListBox<GroupType>(new Renderer<GroupType>() {
			private String getValue(GroupType gt) {
				if (gt == null) {
					return "Mostrar Todos";
				}
				return GroupTypeUtils.getDescription(gt);
			}
			
			@Override
			public String render(GroupType object) {
				return getValue(object);
			}
			
			@Override
			public void render(GroupType object, Appendable appendable) throws IOException {
				appendable.append(getValue(object));
			}
		});
		groupTypes.addValueChangeHandler(new ValueChangeHandler<GroupType>() {
			@Override
			public void onValueChange(ValueChangeEvent<GroupType> event) {
				if (groupTypesSelection != null) {
					GroupType gt = groupTypes.getValue();
					groupTypesSelection.setSelected(gt, true);
				}
			}
		});			
	}	
	
	public GroupList() {
		createFilterGroup();
		createGroups();
		createGroupTypes();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		groupsData = new ArrayList<Group>();
		groupsFilteredData = new ArrayList<Group>();		
	}
	
	
	@Override
	public void clear() {
		groupsCount.setText("0");
		filterGroup.setText("");
		
		groupTypesCache.clear();
		groupTypes.setAcceptableValues(groupTypesCache);
		
		filterTimerGroups = null;
		groupsFilteredData.clear();
		
		groups.setRowCount(0);		
		groupsData.clear();
		groups.setRowData(groupsData);
	}
	
	private void filterGroups() {
		String ft = filterGroup.getText();
		if (ft == null || ft.trim().equals("")) {
			groupsCount.setText(String.valueOf(groupsData.size()));
			groups.setRowData(groupsData);
			return;
		}

		groupsFilteredData.clear();
		for (Group g : groupsData) {
			for (FilterGroup f : filtersGroups) {
				if (f.checkFilter(g, ft)) {
					groupsFilteredData.add(g);
					break;
				}
			}
		}
		groupsCount.setText(String.valueOf(groupsFilteredData.size()));
		groups.setRowData(groupsFilteredData);
	}	
	
	@Override
	public void setGroups(List<Group> groups) {
		groupsData.clear();
		if (groups == null) {
			return;
		}
		GroupSort.sort(groups);
		groupsData.addAll(groups);
		filterGroups();	
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setSelectionModel(SingleSelectionModel<Group> group) {
		groups.setSelectionModel(group);
	}
	
	@Override
	public void setGroupTypeSelectionModel(SingleSelectionModel<GroupType> selection) {
		groupTypesSelection = selection;
	}
	
	@Override
	public void setGroupTypes(List<GroupType> types) {
		
		groupTypesCache.clear();
		
		if (types == null) {
			return;
		}

		EnumGroupTypesSort.sort(types);
		groupTypesCache.addAll(types);
		
		setGroupTypesFilter(types);
	}	
	
	private void setGroupTypesFilter(List<GroupType> types) {
		List<GroupType> valuesTypes = new ArrayList<GroupType>();
		valuesTypes.add(null);
		valuesTypes.addAll(types);

		groupTypes.setValue(null);
		groupTypes.setAcceptableValues(valuesTypes);
		if (groupTypesSelection != null) {
			groupTypesSelection.setSelected(null,true);
		}			
	}	

}
