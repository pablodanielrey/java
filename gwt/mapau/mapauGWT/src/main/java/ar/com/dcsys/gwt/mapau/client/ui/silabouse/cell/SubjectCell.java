package ar.com.dcsys.gwt.mapau.client.ui.silabouse.cell;

import ar.com.dcsys.data.silabouse.Subject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


public class SubjectCell  extends AbstractCell<Subject>{

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,	Subject value, SafeHtmlBuilder sb) {
	    if (value == null) {
	            return;
	    }
	
	    String valueName = value.getName() ;
	    if (valueName == null || valueName.equals("")) {
	            valueName = "-";
	    }
	
	    sb.appendEscaped(valueName);
		
	}

}
