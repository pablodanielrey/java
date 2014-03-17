package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PinAuthDataPlace extends Place {

	public PinAuthDataPlace(String token) { }
	
	public PinAuthDataPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<PinAuthDataPlace> {

		@Override
		public PinAuthDataPlace getPlace(String token) {
			return new PinAuthDataPlace();
		}

		@Override
		public String getToken(PinAuthDataPlace place) {
			return null;
		}
		
	}
	
}
