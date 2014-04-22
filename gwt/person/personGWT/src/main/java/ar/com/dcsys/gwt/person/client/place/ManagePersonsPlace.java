package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ManagePersonsPlace extends Place {
	
	public ManagePersonsPlace(String token) { }
	
	public ManagePersonsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<ManagePersonsPlace> {

		@Override
		public ManagePersonsPlace getPlace(String token) {
			return new ManagePersonsPlace();
		}

		@Override
		public String getToken(ManagePersonsPlace place) {
			return null;
		}
		
	}
}
