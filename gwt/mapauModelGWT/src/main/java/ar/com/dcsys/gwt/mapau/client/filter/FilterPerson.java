package ar.com.dcsys.gwt.mapau.client.filter;

import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter.TransferFilterProvider;

public class FilterPerson implements Filter<Person> {

	private Person value;
	
	public FilterPerson(Person value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.getLastName() + ", " + value.getName();
	}
	
	@Override
	public Person getValue() {
		return value;
	}
	
	@Override
	public void setValue(Person value) {
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
		
		if (!(o.getValue() instanceof Person)) {
			return 1;
		} 
		
		String idO = ((Person)o.getValue()).getId();
		
		return this.getValue().getId().compareTo(idO);
	}	
	
	@Override
	public TransferFilterType getType() {
		return TransferFilterType.PERSON;
	}
	
	@Override
	public TransferFilter getTransferFilter(TransferFilterProvider tp) {
		TransferFilter tfp = tp.newTransferFilter();
		tfp.setType(TransferFilterType.PERSON);
		tfp.setParam(getValue().getId());
		return tfp;
	}
	
}
