package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ManageJustificationPlace extends Place {
	
	public ManageJustificationPlace(String token) { }
	
	public ManageJustificationPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<ManageJustificationPlace> {

		@Override
		public ManageJustificationPlace getPlace(String token) {
			return new ManageJustificationPlace();
		}

		@Override
		public String getToken(ManageJustificationPlace place) {
			return null;
		}
		
	}

}
