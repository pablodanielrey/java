package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PersonDataPlace extends Place {
	
	public PersonDataPlace(String token) { }
	
	public PersonDataPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<PersonDataPlace> {

		@Override
		public PersonDataPlace getPlace(String token) {
			return new PersonDataPlace();
		}

		@Override
		public String getToken(PersonDataPlace place) {
			return null;
		}
		
	}
}
