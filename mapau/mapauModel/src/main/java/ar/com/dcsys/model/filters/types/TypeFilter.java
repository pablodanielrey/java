package ar.com.dcsys.model.filters.types;

import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.model.filters.PropertyAccesor;
import ar.com.dcsys.model.filters.PropertyFilter;
import ar.com.dcsys.model.utils.ReserveAttemptTypeComparator;

public class TypeFilter extends PropertyFilter<ReserveAttemptDateType> {

	private static final ReserveAttemptTypeComparator c = new ReserveAttemptTypeComparator();
	
	private static final PropertyAccesor<ReserveAttemptDateType> pc = new PropertyAccesor<ReserveAttemptDateType>() {
		@Override
		public boolean multipleValues() {
			return false;
		}

		@Override
		public List<ReserveAttemptDateType> getProperties(ReserveAttemptDate ra) throws FilterException {
			throw new FilterException("valores múltiples no permitidos");
		}
		
		@Override
		public ReserveAttemptDateType getProperty(ReserveAttemptDate rad) throws FilterException {
			ReserveAttemptDateType type = rad.getType();
			if (type == null) {
				throw new FilterException();
			}
			return type;
		}
	};
	
	public TypeFilter(ReserveAttemptDateType type) throws FilterException {
		super(type, c, pc);
	}

}
