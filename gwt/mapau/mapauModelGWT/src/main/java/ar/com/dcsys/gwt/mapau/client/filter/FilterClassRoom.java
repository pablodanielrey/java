package ar.com.dcsys.gwt.mapau.client.filter;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter.TransferFilterProvider;

public class FilterClassRoom implements Filter<ClassRoom> {

	private ClassRoom value;
	
	public FilterClassRoom(ClassRoom value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.getName();
	}
	
	@Override
	public ClassRoom getValue() {
		return value;
	}
	
	@Override
	public void setValue(ClassRoom value) {
		this.value = value;
	}
	
	@Override
	public int compareTo(Filter<?> o) {
		if (this.getValue() == null || this.getValue().getId() == null) {
			return -1;
		}
							
		if (o == null || o.getValue() == null) {
			return 1;
		}
		
		if (!(o.getValue() instanceof ClassRoom)) {
			return 1;
		} 
		
		String idO = ((ClassRoom)o.getValue()).getId();
		
		return this.getValue().getId().compareTo(idO);
	}	
	
	@Override
	public TransferFilterType getType() {
		return TransferFilterType.CLASSROOM;
	}
	
	@Override
	public TransferFilter getTransferFilter(TransferFilterProvider tp) {
		TransferFilter tfp = tp.newTransferFilter();
		tfp.setType(TransferFilterType.CLASSROOM);
		tfp.setParam(getValue().getId());
		return tfp;
	}
	
}
