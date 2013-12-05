package ar.com.dcsys.model.filters.types;

import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.model.filters.PropertyAccesor;
import ar.com.dcsys.model.filters.PropertyFilter;
import ar.com.dcsys.model.utils.CourseComparator;

public class CourseFilter extends PropertyFilter<Course> {

	private static final CourseComparator cc = new CourseComparator();
	
	private static final PropertyAccesor<Course> pc = new PropertyAccesor<Course>() {
		@Override
		public boolean multipleValues() {
			return false;
		}
		
		@Override
		public List<Course> getProperties(ReserveAttemptDate ra) throws FilterException {
			throw new FilterException("sin valores múltiples");
		}
		
		@Override
		public Course getProperty(ReserveAttemptDate ra) throws FilterException {
			if (ra.getCourse() == null) {
				throw new FilterException();
			}
			return ra.getCourse();
		}
	};
	
	public CourseFilter(Course course) throws FilterException {
		super(course, cc, pc);
	}

}
