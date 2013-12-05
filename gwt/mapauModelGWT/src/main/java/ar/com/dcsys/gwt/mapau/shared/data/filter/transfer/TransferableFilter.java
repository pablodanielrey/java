package ar.com.dcsys.gwt.mapau.shared.data.filter.transfer;

import ar.com.dcsys.data.filter.TransferFilter;


public interface TransferableFilter {

	public interface TransferFilterProvider {
		public TransferFilter newTransferFilter();
	}	
	
	TransferFilter getTransferFilter(TransferFilterProvider tp);
	
}
