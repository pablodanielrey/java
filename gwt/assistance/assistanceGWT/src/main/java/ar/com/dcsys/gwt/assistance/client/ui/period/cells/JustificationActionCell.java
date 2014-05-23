package ar.com.dcsys.gwt.assistance.client.ui.period.cells;

import java.util.LinkedList;
import java.util.List;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.assistance.client.ui.cell.JustificationCell;
import ar.com.dcsys.gwt.assistance.client.ui.common.cell.HasCellContainer;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class JustificationActionCell extends CompositeCell<Report> {
	
	public JustificationActionCell(List<HasCell<Report, ?>> hasCells) {
		super(hasCells);
	}
	
	public static JustificationActionCell createCell(final JustificationActionCellPresenter<Report> p) {
		// action cell que se muestra y maneja los eventos cuando tiene justificaciones
		ActionCell<Report> deleteJustify = new ActionCell<Report>("X", new Delegate<Report>() {
			@Override
			public void execute(Report object) {
				if (object == null || p == null) {
					return;
				}
				JustificationDate justifications = p.getJustifications(object);
				if (justifications == null) {
					// NO DEBERÍA SER NUNCA, PERO POR LAS DUDAS
					return;
				}
				p.deleteJustification(justifications);
			}
		}) {
			@Override
			public void onBrowserEvent(Context context,Element parent, Report value, NativeEvent event,ValueUpdater<Report> valueUpdater) {				
				if (value == null) {
					super.onBrowserEvent(context, parent, value, event, valueUpdater);
					return;
				}
				
				JustificationDate justifications = p.getJustifications(value);
				if (justifications == null) {
					// si no tiene justificaciones entonces no hago nada en el click
					return;
				}
				
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			};
			
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	Report value, SafeHtmlBuilder sb) {
				if (value == null) {
					super.render(context, value, sb);
					return;
				}
				
				JustificationDate justifications = p.getJustifications(value);
				if (justifications == null) {
					// si no tiene justificaciones entonces no hago nada en el click
					return;
				}
				
				super.render(context, value, sb);
			};
		};
		
		//cell que se muestra cuando existen justificaciones
		AbstractCell<Report> justificationExistCell = new AbstractCell<Report> () {
			private final JustificationCell jc = new JustificationCell();
			
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	Report value, SafeHtmlBuilder sb) {
				JustificationDate justifications = p.getJustifications(value);
				if (justifications == null) {
					// NO DEBERÍA SER NUNCA, PERO POR LAS DUDAS
					return;
				}
				jc.render(context, justifications.getJustification(), sb);
			}
		};
		
		//cell que se muestra cuando no existen justificaciones
		ActionCell<Report> justify = new ActionCell<Report>("J", new Delegate<Report>() {
			@Override
			public void execute(Report object) {
				if (object == null || p == null) {
					return;
				}
				p.justify();
			}
		}) {
			@Override
			public void onBrowserEvent(Context context,Element parent, Report value, NativeEvent event,ValueUpdater<Report> valueUpdater) {			
				if (value == null) {
					super.onBrowserEvent(context, parent, value, event, valueUpdater);
					return;
				}
				JustificationDate justifications = p.getJustifications(value);
				if (justifications == null) {
					super.onBrowserEvent(context, parent, value, event, valueUpdater);
					return;
				}
			};
			
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	Report value, SafeHtmlBuilder sb) {
				if (value == null) {
					super.render(context, value, sb);
					return;
				}
				
				JustificationDate justifications = p.getJustifications(value);
				if (justifications == null) {
					super.render(context, value, sb);
					return;
				}
			};
		};
		
		List<HasCell<Report,?>> cells = new LinkedList<HasCell<Report,?>>();
		cells.add(new HasCellContainer<Report>(deleteJustify));
		cells.add(new HasCellContainer<Report>(justify));
		cells.add(new HasCellContainer<Report>(justificationExistCell));
		
		return new JustificationActionCell(cells);
	}

}
