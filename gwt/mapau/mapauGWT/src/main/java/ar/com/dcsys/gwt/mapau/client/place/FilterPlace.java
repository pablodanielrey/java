package ar.com.dcsys.gwt.mapau.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class FilterPlace extends Place {
	
	public FilterPlace(String token) { }
	
	public FilterPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<FilterPlace> {
		
		@Override
		public FilterPlace getPlace(String token) {
			return new FilterPlace();
		}
		
		@Override
		public String getToken(FilterPlace place) {
			return null;
		}
		
	}

}
