package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;


public class FilterTypeCourse implements FilterType<Course> {
	
	public static final Logger logger = Logger.getLogger(FilterTypeCourse.class.getName());
	
	private final List<Filter<?>> values;
	private boolean visible;
	
	private class FilterValueCourse implements FilterValue<Course> {
		
		private FilterCourse selected;
		private FilterType<?> filterType;
		private boolean visible;
		
		public FilterValueCourse(FilterType<?> filterType) {
			this.filterType = filterType;
			this.visible = true;
		}
		
		@Override
		public FilterType<?> getFilterType() {
			return filterType;
		}
		
		@Override
		public String getName() {
			return "Materia";
		}
		
		@Override
		public Filter<Course> getSelected() {
			return selected;
		}
		
		@Override
		public void setSelected(Filter<?> selected) {
			if (selected instanceof FilterCourse) {
				this.selected = (FilterCourse) selected;
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
	
	public FilterTypeCourse(SilegManager rf) {
		values = new ArrayList<Filter<?>>();
		this.setValues(rf);
		visible = true;
	}
	
	private void setValues(SilegManager rf) {
		rf.findAllCourses(new Receiver<List<Course>>() {
			
			@Override
			public void onSuccess(List<Course> courses) {
				values.clear();
				for (Course c : courses) {
					values.add(new FilterCourse(c));
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
		return "Materia";
	}
	
	@Override
	public FilterValue<Course> createNewFilter() {
		return new FilterValueCourse(this);
	}
	
	@Override
	public String getClassNameFilter() {
		return FilterCourse.class.getName();
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
