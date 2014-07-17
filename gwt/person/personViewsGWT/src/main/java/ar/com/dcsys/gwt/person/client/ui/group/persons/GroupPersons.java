package ar.com.dcsys.gwt.person.client.ui.group.persons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class GroupPersons extends Composite implements GroupPersonsView {

	private static GroupPersonsUiBinder uiBinder = GWT.create(GroupPersonsUiBinder.class);

	interface GroupPersonsUiBinder extends UiBinder<Widget, GroupPersons> {
	}

	@UiField FlowPanel membersView;
	@UiField FlowPanel outPersonsView;
	
	@UiField Button add;
	@UiField Button remove;
	
	private GroupPersonsView.Presenter presenter;
	
	public GroupPersons() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Panel getMembersPanel() {
		return membersView;
	}
	
	@Override
	public Panel getOutPersonsPanel() {
		return outPersonsView;
	}
	
	@UiHandler("add")
	public void onAdd(ClickEvent event) {
		presenter.add();
	}
	
	@UiHandler("remove")
	public void onRemove(ClickEvent event) {
		presenter.remove();
	}
	
}
