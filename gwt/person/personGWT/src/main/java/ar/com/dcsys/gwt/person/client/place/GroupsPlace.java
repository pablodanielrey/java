package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class GroupsPlace extends Place {
	
	public GroupsPlace(String token) { }
	
	public GroupsPlace() { }
	
	public static class Tokenizer implements PlaceTokenizer<GroupsPlace> {

		@Override
		public GroupsPlace getPlace(String token) {
			return new GroupsPlace();
		}

		@Override
		public String getToken(GroupsPlace place) {
			return null;
		}
		
	}
}
