package ar.com.dcsys.gwt.person.client.ui.group.manage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ManageGroup extends Composite implements ManageGroupView {

	private static ManageGroupUiBinder uiBinder = GWT.create(ManageGroupUiBinder.class);

	interface ManageGroupUiBinder extends UiBinder<Widget, ManageGroup> {
	}

	@UiField Button createGroup;
	@UiField FlowPanel groupListView;
	@UiField FlowPanel personsView;
	
	private final DialogBox dialogGroupDataView;
	private final Button commitGroup;
	private final Button cancelGroup;
	private final FlowPanel groupDataView;
	private final FlowPanel groupPanelDataView;
	private final FlowPanel groupBtnPanelDataView;
	
	private ManageGroupView.Presenter presenter;
	
	public ManageGroup() {
		groupPanelDataView = new FlowPanel();
		
		groupDataView = new FlowPanel(); 		
		
		dialogGroupDataView = new DialogBox();
		dialogGroupDataView.setGlassEnabled(true);
		dialogGroupDataView.setModal(true);
		dialogGroupDataView.setAnimationEnabled(true);
		
		groupBtnPanelDataView = new FlowPanel();
		
		commitGroup = new Button();
		commitGroup.setText("Guardar");
		commitGroup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				presenter.persist();				
			}			
		});		
		groupBtnPanelDataView.add(commitGroup);
		
		cancelGroup = new Button();
		cancelGroup.setText("Cancelar");
		cancelGroup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				closeGroupDataPanel();
			}
			
		});
		groupBtnPanelDataView.add(cancelGroup);
		
		groupPanelDataView.add(groupDataView);
		groupPanelDataView.add(groupBtnPanelDataView);
		dialogGroupDataView.add(groupPanelDataView);	
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void clear() {
		closeGroupDataPanel();
	}
	
	@Override
	public Panel getGroupDataPanel() {
		return groupDataView;
	}
	
	@Override
	public Panel getGroupListPanel() {
		return groupListView;
	}
	
	@Override
	public Panel getPersonsPanel() {
		return personsView;
	}
	
	@Override
	public void closeGroupDataPanel() {
		dialogGroupDataView.hide();
	}
	
	@UiHandler("createGroup")
	public void onCreateGroup(ClickEvent event) {
		dialogGroupDataView.center();
		dialogGroupDataView.show();	
	}	
	
	@Override
	public void changeTextCreateButton(String text) {
		createGroup.setText(text);
	}

}
