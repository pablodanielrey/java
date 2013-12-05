package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;
import ar.com.dcsys.utils.PersonSort;

public class FilterTypePerson implements FilterType<Person> {
	
	private static final Logger logger = Logger.getLogger(FilterTypePerson.class.getName());
	
	private final List<Filter<?>> values;
	private boolean visible;

	private class FilterValuePerson implements FilterValue<Person> {
		
		private FilterPerson selected; 
		private FilterType<?> filterType;
		private boolean visible;
		
		public FilterValuePerson (FilterType<?> filterType) {
			this.filterType = filterType;
			this.visible = true;
		}
		
		@Override
		public FilterType<?> getFilterType() {
			return filterType;
		}
		
		@Override
		public String getName() {
			return "Persona";
		}
		
		@Override
		public Filter<Person> getSelected() {
			return selected;
		}
		
		@Override
		public void setSelected(Filter<?> selected) {
			if (selected instanceof FilterPerson) {
				this.selected = (FilterPerson) selected;
			}
		}
		
		@Override
		public boolean isVisible() {
			return visible;
		}
		
		@Override
		public void setVisible(boolean v) {
			this.visible = v;
		}
		
	}
	
	public FilterTypePerson(SilegManager rf) {
		values = new ArrayList<Filter<?>>();
		setValues(rf);
		visible = true;
	}
	
	private void setValues(SilegManager rf) {
		rf.findAllTeachers(new Receiver<List<Person>>(){			
			@Override
			public void onSuccess(List<Person> persons) {
				values.clear();
				PersonSort.sort(persons);
				for (Person p : persons) {
					values.add(new FilterPerson(p));
				}				
			}
			@Override
			public void onFailure(Throwable error) {
				logger.log(Level.SEVERE,error.getMessage(),error);
			}
		});		
	}
	
	@Override
	public List<Filter<?>> getValues() {
		return values;
	}
	
	@Override
	public String getName() {
		return "Persona";
	}
	
	@Override
	public FilterValue<Person> createNewFilter() {
		return new FilterValuePerson(this); 
	}
	
	@Override
	public String getClassNameFilter() {
		return FilterPerson.class.getName();
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean v) {
		this.visible = v;
	}
}
