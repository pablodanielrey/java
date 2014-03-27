package ar.com.dcsys.gwt.assistance.client.ui.justification.widget.justificationList;

import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.JustificationListWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;

public class JustificationList extends Composite implements JustificationListWidget{

	private static JustificationListUiBinder uiBinder = GWT.create(JustificationListUiBinder.class);

	interface JustificationListUiBinder extends UiBinder<Widget, JustificationList> {
	}
	
	@UiField (provided=true) DataGrid<Justification> justifications;

	private void createJustification() {
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
		TextColumn<Justification> description = new TextColumn<Justification>() {
			@Override
			public String getValue(Justification object) {
				String description = object.getCode();
				if (description == null) {
					return "no tiene";
				}
				return description;
			}
		};	
		
		justifications = new DataGrid<Justification>();
		justifications.addColumn(code,"Código");
		justifications.addColumn(description,"Descripción");
	}
	
	public JustificationList() {
		createJustification();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setJustification(List<Justification> justifications) {
		this.justifications.setRowData(justifications);
	}

	@Override
	public void clear() {
		justifications.setRowCount(0,true);
	}

	@Override
	public void setSelectionModel(SelectionModel<Justification> selection) {
		justifications.setSelectionModel(selection);
	}

}
