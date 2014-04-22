package ar.com.dcsys.gwt.mapau.client.filter;

import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;

public class FilterCourse implements Filter<Course> {

	private Course value;
	
	public FilterCourse (Course course) {
		this.value = course;
	}
	
	@Override
	public String toString() {
		Subject s = value.getSubject();
		String subjectStr = (s == null || s.getName() == null) ? "" : s.getName();					
		if (value.getName() == null) {
			if (subjectStr.equals("")) {
				return "No posee nombre";
			}
			return subjectStr;
		}
		return subjectStr + " " + value.getName();
	}
	
	@Override
	public Course getValue() {
		return value;
	}
	
	@Override
	public void setValue(Course value) {
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
		
		if (!(o.getValue() instanceof Course)) {
			return 1;
		} 
		
		String idO = ((Course)o.getValue()).getId();
		
		return this.getValue().getId().compareTo(idO);
	}
	
	
	@Override
	public TransferFilterType getType() {
		return TransferFilterType.COURSE;
	}
	
	@Override
	public TransferFilter getTransferFilter(TransferFilterProvider tp) {
		TransferFilter tfp = tp.newTransferFilter();
		tfp.setType(TransferFilterType.COURSE);
		tfp.setParam(getValue().getId());
		return tfp;
	}
	
}
