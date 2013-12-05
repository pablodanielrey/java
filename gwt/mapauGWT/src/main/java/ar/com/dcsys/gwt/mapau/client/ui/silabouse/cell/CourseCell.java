package ar.com.dcsys.gwt.mapau.client.ui.silabouse.cell;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.Subject;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


public class CourseCell extends AbstractCell<Course>{

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,Course value, SafeHtmlBuilder sb) {
	    if (value == null) {
            return;
	    }
	
	    Subject subject = value.getSubject();
	    String valueName = subject.getName() +" - "+ value.getName();
	    if (valueName == null || valueName.equals("")) {
	            valueName = "-";
	    }
	
	    sb.appendEscaped(valueName);
		
	}

}
