package ar.com.dcsys.gwt.assistance.client.ui.cell;

import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class JustificationCell extends AbstractCell<Justification> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, Justification value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		
		String code = "Sin-Código";
		String description = "";
		
		if (value.getCode() != null) {
			code = value.getCode();
		}
		
		if (value.getDescription() != null) {
			description = value.getDescription();
		}
		
		sb.appendHtmlConstant("<span>");
				sb.appendEscaped(code);
		sb.appendHtmlConstant("</span>");
	}

}
