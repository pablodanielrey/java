package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SyncPersonsPlace extends Place {

	public SyncPersonsPlace(String token) { }
	
	public SyncPersonsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<SyncPersonsPlace> {

		@Override
		public SyncPersonsPlace getPlace(String token) {
			return new SyncPersonsPlace();
		}

		@Override
		public String getToken(SyncPersonsPlace place) {
			return null;
		}
		
	}
	
}
