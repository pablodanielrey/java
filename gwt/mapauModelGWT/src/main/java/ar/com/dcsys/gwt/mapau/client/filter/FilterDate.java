package ar.com.dcsys.gwt.mapau.client.filter;

import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter.TransferFilterProvider;

public class FilterDate implements Filter<DateValue> {

	private DateValue value;
	
	public FilterDate (DateValue date) {
		this.value = date;
	}
	
	@Override
	public TransferFilterType getType() {
		return TransferFilterType.DATE;
	}
	
	@Override
	public TransferFilter getTransferFilter(TransferFilterProvider tp) {
		
		TransferFilter tfp = tp.newTransferFilter();
		
		tfp.setType(TransferFilterType.DATE);
				
		if (value == null || value.getStart() == null || value.getEnd() == null) {
			return null;
		}
		String startStr = String.valueOf(value.getStart().getTime());
		String endStr = String.valueOf(value.getEnd().getTime());
		tfp.setParam(startStr + ";" + endStr);
		
		return tfp;
	}

	@Override
	public DateValue getValue() {
		return value;
	}

	@Override
	public void setValue(DateValue value) {
		this.value = value;
	}

	@Override
	public int compareTo(Filter<?> o) {
		if (o == null || o.getValue() == null) {
			if (this.getValue() == null) {
				return 0;
			}
			return 1;
		}
		
		if (this.getValue() == null) {
			return -1;
		}
		
		if (!(o.getValue() instanceof DateValue)) {
			return 1;
		}
				
		return this.getValue().compareTo((DateValue)o.getValue());
	}

}
