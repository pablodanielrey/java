package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class JustificationPersonPlace extends Place {
	
	public JustificationPersonPlace(String token) { }
	
	public JustificationPersonPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<JustificationPersonPlace> {

		@Override
		public JustificationPersonPlace getPlace(String token) {
			return new JustificationPersonPlace();
		}

		@Override
		public String getToken(JustificationPersonPlace place) {
			return null;
		}
		
	}
}
