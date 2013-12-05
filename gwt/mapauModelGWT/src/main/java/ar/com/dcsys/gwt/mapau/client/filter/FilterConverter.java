package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter.TransferFilterProvider;
/**
 * 
 * @author emanuel
 * Clase que mapea de FilterTypeValues, el enumerativo, a FilterType y viceversa
 */

public class FilterConverter {
	
	private final SilegManager silegManager;
	private final ClassRoomsManager classRoomsManager;
	
	@Inject
	public FilterConverter(SilegManager silegManager, ClassRoomsManager classRoomsManager) {
		this.silegManager = silegManager;
		this.classRoomsManager = classRoomsManager;
	}
	
	
	public List<FilterType<?>> enumToFilterTypes(List<TransferFilterType> enums) {
		List<FilterType<?>> filters = new ArrayList<FilterType<?>>(enums.size());
		
		for (TransferFilterType value : enums) {
			switch (value) {
				case CLASSROOM: 
					filters.add(new FilterTypeClassRoom(classRoomsManager)); 
					break;
					
				case COURSE: 
					filters.add(new FilterTypeCourse(silegManager)); 
					break;
				case DATE:
					filters.add(new FilterTypeDate());
					break;
				case PERSON:
					filters.add(new FilterTypePerson(silegManager));
					break;
			}			
		}
		
		return filters;
	}
	
	
	public List<TransferFilter> convertFilters(TransferFilterProvider tp, List<FilterValue<?>> filters) {

		List<TransferFilter> filtersValues = new ArrayList<TransferFilter>();
		for (FilterValue<?> value : filters) {
			Filter<?> selectedFilter = value.getSelected();
			TransferFilter newFilter = selectedFilter.getTransferFilter(tp);
			filtersValues.add(newFilter);						
		}
		return filtersValues;
	}
	
}
