package ar.com.dcsys.gwt.mapau.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class CalendarPlace extends Place {
	
	public CalendarPlace(String token) { }
	
	public CalendarPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<CalendarPlace> {

		@Override
		public CalendarPlace getPlace(String token) {
			return new CalendarPlace();
		}

		@Override
		public String getToken(CalendarPlace place) {
			return null;
		}
		
	}
}
