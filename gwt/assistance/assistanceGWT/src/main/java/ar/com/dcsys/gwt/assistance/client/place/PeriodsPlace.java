package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PeriodsPlace extends Place {
	
	public PeriodsPlace(String token) { }
	
	public PeriodsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<PeriodsPlace> {

		@Override
		public PeriodsPlace getPlace(String token) {
			return new PeriodsPlace();
		}

		@Override
		public String getToken(PeriodsPlace place) {
			return null;
		}
		
	}

}
