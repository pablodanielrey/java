package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class DailyPeriodsPlace extends Place {

	public DailyPeriodsPlace(String token) { }
	
	public DailyPeriodsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<DailyPeriodsPlace> {

		@Override
		public DailyPeriodsPlace getPlace(String token) {
			return new DailyPeriodsPlace();
		}

		@Override
		public String getToken(DailyPeriodsPlace place) {
			return null;
		}
		
	}
	
}
