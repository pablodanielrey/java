package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PeriodsAssignationPersonPlace extends Place {
	
	public PeriodsAssignationPersonPlace(String token) { }
	
	public PeriodsAssignationPersonPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<PeriodsAssignationPersonPlace> {

		@Override
		public PeriodsAssignationPersonPlace getPlace(String token) {
			return new PeriodsAssignationPersonPlace();
		}

		@Override
		public String getToken(PeriodsAssignationPersonPlace place) {
			return null;
		}
		
	}

}
