package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class UpdatePersonDataPlace extends Place {
	
	public UpdatePersonDataPlace(String token) { }
	
	public UpdatePersonDataPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<UpdatePersonDataPlace> {

		@Override
		public UpdatePersonDataPlace getPlace(String token) {
			return new UpdatePersonDataPlace();
		}

		@Override
		public String getToken(UpdatePersonDataPlace place) {
			return null;
		}
		
	}
}
