package ar.com.dcsys.gwt.menu.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class MenuPlace extends Place {
	
	public MenuPlace(String token) { }
	
	public MenuPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<MenuPlace> {

		@Override
		public MenuPlace getPlace(String token) {
			return new MenuPlace();
		}

		@Override
		public String getToken(MenuPlace place) {
			return null;
		}
		
	}
}
