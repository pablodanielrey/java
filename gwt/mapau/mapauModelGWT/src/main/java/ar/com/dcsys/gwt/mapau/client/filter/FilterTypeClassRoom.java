package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

public class FilterTypeClassRoom implements FilterType<ClassRoom> {
	
	private static Logger logger = Logger.getLogger(FilterTypeClassRoom.class.getName());
	
	private final List<Filter<?>> values;
	private boolean visible;

	private class FilterValueClassRoom implements FilterValue<ClassRoom> {
		
		private FilterClassRoom selected; 
		private FilterType<?> filterType;
		private boolean visible;
		
		public FilterValueClassRoom (FilterType<?> filterType) {
			this.filterType = filterType;
			this.visible = true;
		}
		
		@Override
		public FilterType<?> getFilterType() {
			return filterType;
		}
		
		@Override
		public String getName() {
			return "Aula";
		}
		
		@Override
		public Filter<ClassRoom> getSelected() {
			return selected;
		}
		
		@Override
		public void setSelected(Filter<?> selected) {
			if (selected instanceof FilterClassRoom) {
				this.selected = (FilterClassRoom) selected;
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
	
	public FilterTypeClassRoom(ClassRoomsManager classRoomsManager) {
		values = new ArrayList<Filter<?>>();
		setValues(classRoomsManager);
		visible = true;
	}
	
	private void setValues(ClassRoomsManager classRoomsManager) {
		classRoomsManager.findAllClassRooms(new Receiver<List<ClassRoom>>(){			
			@Override
			public void onSuccess(List<ClassRoom> classRooms) {
				values.clear();
				for (ClassRoom c : classRooms) {
					values.add(new FilterClassRoom(c));
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
		return "Aula";
	}
	
	@Override
	public FilterValue<ClassRoom> createNewFilter() {
		return new FilterValueClassRoom(this); 
	}
	
	@Override
	public String getClassNameFilter() {
		return FilterClassRoom.class.getName();
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
