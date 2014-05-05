package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class GeneralsJustificationPlace extends Place {
	
	public GeneralsJustificationPlace(String token) { }
	
	public GeneralsJustificationPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<GeneralsJustificationPlace> {

		@Override
		public GeneralsJustificationPlace getPlace(String token) {
			return new GeneralsJustificationPlace();
		}

		@Override
		public String getToken(GeneralsJustificationPlace place) {
			return null;
		}
		
	}

}
