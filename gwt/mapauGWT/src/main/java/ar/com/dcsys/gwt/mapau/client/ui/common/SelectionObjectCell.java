package ar.com.dcsys.gwt.mapau.client.ui.common;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SelectionObjectCell<T> extends AbstractInputCell<T,T> {

	interface Template extends SafeHtmlTemplates {
		@Template("<option value=\"{0}\">{0}</option>")
		SafeHtml deselected(String option);
		
		@Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml selected(String option);
	}

	private static Template template;
	private final Renderer<T> render;
	private final Comparator<T> comparable;
	private final DataProvider<T> dataProvider;

	
	/**
	 * Se usa para retornar todos los valores posibles del mismo tipo que el pasado como parámetro.
	 * O sea la lista de opciones.
	 * @author pablo
	 *
	 * @param <T>
	 */
	public interface DataProvider<T> {
		public List<T> getValues(T value);
	}
	
	/**
	 * Se usa para convertir el objeto en un string representable en el html.
	 * @author pablo
	 *
	 * @param <C>
	 */
	public interface Renderer<C> {
		public String getValue(C object);
	}

	public SelectionObjectCell(Renderer<T> render, Comparator<T> comparable, DataProvider<T> dataProvider) {
	    super(BrowserEvents.CHANGE);
	    if (template == null) {
	      template = GWT.create(Template.class);
	    }
	    this.render = render;
	    this.comparable = comparable;
	    this.dataProvider = dataProvider;
	}
	
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,	Element parent, T value, NativeEvent event,	ValueUpdater<T> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
	    String type = event.getType();
	    if (BrowserEvents.CHANGE.equals(type)) {
	      Object key = context.getKey();
	      SelectElement select = parent.getFirstChild().cast();
	      int index = select.getSelectedIndex();
	      List<T> values = dataProvider.getValues(value); 
	      if (index >= 0 && index < values.size()) {
	    	  T newValue = values.get(index);
	    	  setViewData(key, newValue);
		      finishEditing(parent, newValue, key, valueUpdater);
		      if (valueUpdater != null) {
			        valueUpdater.update(newValue);
		      }
	    	  return;
	      } else {
	    	  clearViewData(key);
	      }
	    }		
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, T value, SafeHtmlBuilder sb) {
	    Object key = context.getKey();
	    T viewData = getViewData(key);
	    if (viewData != null && viewData.equals(value)) {
	      clearViewData(key);
	      viewData = null;
	    }
	    viewData = viewData == null ? value : viewData;

	    sb.appendHtmlConstant("<select tabindex=\"-1\">");
	    List<T> values = dataProvider.getValues(value);
	    for (T option : values) {
	    	String sData = this.render.getValue(option);
			if (viewData != null && comparable.compare(viewData, option) == 0) {
				sb.append(template.selected(sData));
			} else {
				sb.append(template.deselected(sData));
			}
	    }
	    sb.appendHtmlConstant("</select>");
	}
	
}

