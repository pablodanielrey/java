package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SyncLogsPlace extends Place {

	public SyncLogsPlace(String token) { }
	
	public SyncLogsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<SyncLogsPlace> {

		@Override
		public SyncLogsPlace getPlace(String token) {
			return new SyncLogsPlace();
		}

		@Override
		public String getToken(SyncLogsPlace place) {
			return null;
		}
		
	}
	
}
