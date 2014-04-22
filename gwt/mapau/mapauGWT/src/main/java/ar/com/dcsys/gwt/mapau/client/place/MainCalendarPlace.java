package ar.com.dcsys.gwt.mapau.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class MainCalendarPlace extends Place {
	
	public MainCalendarPlace(String token) { }
	
	public MainCalendarPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<MainCalendarPlace> {

		@Override
		public MainCalendarPlace getPlace(String token) {
			return new MainCalendarPlace();
		}

		@Override
		public String getToken(MainCalendarPlace place) {
			return null;
		}
		
	}
}
