package ar.com.dcsys.gwt.person.client.ui;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;


/**
 * Integra todos los datos de la persona de todos los módulos de los sistemas
 * disponibles.
 * 
 * @author pablo
 *
 */
public interface UpdatePersonDataView extends IsWidget {

	public void setPresenter(Presenter p);
	
	public AcceptsOneWidget getBasicPersonData();
	public AcceptsOneWidget getAssistancePersonData();
	
	public interface Presenter {
		public void persist();
		public void findall();
	}
	
}
