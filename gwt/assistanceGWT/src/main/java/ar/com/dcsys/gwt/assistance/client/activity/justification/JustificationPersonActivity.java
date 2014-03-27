package ar.com.dcsys.gwt.assistance.client.activity.justification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class JustificationPersonActivity extends AbstractActivity implements JustificationPersonView.Presenter{

	private static final Logger logger = Logger.getLogger(JustificationPersonActivity.class.getName());
	
/*	private final JustificationPersonView view;
	private final MultiSelectionModel<PersonValueProxy> selection;
	private final MultiSelectionModel<JustificationDate> selectionJustificationDate;
	private final SingleSelectionModel<Justification> selectionJustification;*/
	
	private final List<Justification> justifications = new ArrayList<Justification>();
	private final List<JustificationDate> justificationDateList = new ArrayList<JustificationDate>();
	
	private enum MODE {
		ADMIN, OPERATOR, USER;
	}

	/*Clase necesaria para la estadisticas*/

	public class JustificationStatistic {
		private String name;
		private int count;
		private String description;
		
		public JustificationStatistic (String name, String description) {
			this.setName(name);
			this.setDescription(description);
			this.setCount(0);
		}
		
		public void incrementCount() {
			this.count++;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}	
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearPersonsSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearJustificationSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findBy(Date start, Date end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeJustificationDates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateStatistics() {
		// TODO Auto-generated method stub
		
	}
	
	

}
