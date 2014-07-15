package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ ManagePersonsPlace.Tokenizer.class,
				  UpdatePersonDataPlace.Tokenizer.class,
				  MailChangePlace.Tokenizer.class,
				  PersonReportPlace.Tokenizer.class,
				  GroupsPlace.Tokenizer.class
				  })
public interface PersonPlaceHistoryMapper extends PlaceHistoryMapper {

}
