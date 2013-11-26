package ar.com.dcsys.gwt.person.client.ui;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public class AcceptsOneWidgetAdapter implements AcceptsOneWidget {

	private Panel panel;
	public AcceptsOneWidgetAdapter(Panel panel) {
		this.panel = panel;
	}
	@Override
	public void setWidget(IsWidget w) {
		panel.clear();
		panel.add(w);
	}	
	
}
