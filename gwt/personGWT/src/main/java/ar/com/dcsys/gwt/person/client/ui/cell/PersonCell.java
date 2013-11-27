package ar.com.dcsys.gwt.person.client.ui.cell;

import ar.com.dcsys.gwt.person.shared.PersonProxy;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


public class PersonCell extends AbstractCell<PersonProxy> {

	private final String imageHtml;
	
	public PersonCell(String imageHtml) {
		super();
		this.imageHtml = imageHtml;
	}
	
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, PersonProxy person, SafeHtmlBuilder sb) {
		if (person == null) {
			return;
		}
		
		sb.appendHtmlConstant("<table>");
			sb.appendHtmlConstant("<tr>");
				sb.appendHtmlConstant("<td rowspan='3'>");
				sb.appendHtmlConstant(imageHtml);
				sb.appendHtmlConstant("</td>");
				
				sb.appendHtmlConstant("<tr>");
					sb.appendHtmlConstant("<td style='font-size:95%;'>");
						sb.appendEscaped(person.getLastName() == null ? "nulo" : person.getLastName());
					sb.appendHtmlConstant("</td>");
					sb.appendHtmlConstant("<td style='font-size:95%;'>");
						sb.appendEscaped(person.getName() == null ? "nulo" : person.getName());
					sb.appendHtmlConstant("</td>");
				sb.appendHtmlConstant("</tr>");
				
				sb.appendHtmlConstant("<tr>");
					sb.appendHtmlConstant("<td style='font-size:95%;'>");
						sb.appendEscaped(person.getDni() == null ? "" : person.getDni());
					sb.appendHtmlConstant("</td>");
				sb.appendHtmlConstant("</tr>");
				
			sb.appendHtmlConstant("</tr>");
		sb.appendHtmlConstant("</table>");
		
	}
	

}
