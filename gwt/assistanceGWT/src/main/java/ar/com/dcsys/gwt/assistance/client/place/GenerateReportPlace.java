package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class GenerateReportPlace extends Place {
	public GenerateReportPlace(String token) { }
	
	public GenerateReportPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<GenerateReportPlace> {

		@Override
		public GenerateReportPlace getPlace(String token) {
			return new GenerateReportPlace();
		}

		@Override
		public String getToken(GenerateReportPlace place) {
			return null;
		}
		
	}
}
