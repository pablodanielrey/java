package ar.com.dcsys.gwt.assistance.client.ui.justification.manage;

import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.client.common.JustificationsSort;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;

public class ManageJustifications extends Composite implements ManageJustificationsView{

	private static ManageJustificationsUiBinder uiBinder = GWT	.create(ManageJustificationsUiBinder.class);

	interface ManageJustificationsUiBinder extends UiBinder<Widget, ManageJustifications> {
	}

	@UiField (provided=true) DataGrid<Justification> justifications;
	@UiField Button cancel;
	@UiField Button commit;
	@UiField (provided=true) TextArea description;
	@UiField (provided=true) TextBox code;
	
	private Presenter p;
	
	private void createJustification () {
		TextColumn<Justification> code = new TextColumn<Justification>() {
			@Override
			public String getValue(Justification object) {
				String code = object.getCode();
				if (code == null) {
					return "no tiene";
				}
				return code;
			}
		};
		TextColumn<Justification> desc = new TextColumn<Justification>() {
			@Override
			public String getValue(Justification object) {
				String desc = object.getDescription();
				if (desc == null) {
					return "no tiene";
				}
				return desc;
			}
		};	
		
		ActionCell<Justification> removeC = new ActionCell<Justification>("eliminar",new ActionCell.Delegate<Justification>() {
			public void execute(Justification j) {
				p.remove();
			};
		});
		Column<Justification, ActionCell<Justification>> remove = new IdentityColumn(removeC);
		
		justifications = new DataGrid<Justification>();
		justifications.addColumn(code,"Código");
		justifications.addColumn(desc,"Descripción");
		justifications.addColumn(remove);
	}
	
	private void createCode() {
		code = new TextBox();
		code.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				setEnabled(code.getText() != "" && description.getText() != "");	
			}
		});		
	}
	
	private void createDescription() {
		description = new TextArea();
		description.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				setEnabled(code.getText() != "" && description.getText() != "");	
			}
		});			
	}
	
	public ManageJustifications () {
		createCode();
		createDescription();
		createJustification();		
		initWidget(uiBinder.createAndBindUi(this));
		setEnabled(false);
	}
	
	@UiHandler("cancel")
	public void onCancel (ClickEvent event) {
		p.cancel();
	}
	
	
	@Override
	public void setActionText(String action) {
		commit.setText(action);
	}
	
	@UiHandler("commit")
	public void onCommit (ClickEvent event) {
		p.commit();
	}
	
	@Override
	public void setEnabled(boolean b) {		
		commit.setEnabled(b);
	}

	@Override
	public void clear() {
		clearData();
		justifications.setRowCount(0,true);
	}

	@Override
	public void clearData() {
		setEnabled(false);
		code.setText("");
		description.setText("");
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void setJustifications(List<Justification> justifications) {
		this.justifications.setRowCount(0,true);
		if (justifications == null) {
			return;
		}
		JustificationsSort.sort(justifications);		
		this.justifications.setRowData(justifications);
	}

	@Override
	public void setSelectionJustification (SelectionModel<Justification> selection) {
		justifications.setSelectionModel(selection);
	}

	@Override
	public void setJustification(Justification justification) {
		if (justification == null) {
			return;
		}		
		if (justification.getCode() != null) {
			code.setText(justification.getCode());
		}		
		if (justification.getDescription() != null) {
			description.setText(justification.getDescription());
		}
		
	}

	@Override
	public String getCode() {
		return code.getText();
	}

	@Override
	public String getDescription() {
		return description.getText();
	}

}
