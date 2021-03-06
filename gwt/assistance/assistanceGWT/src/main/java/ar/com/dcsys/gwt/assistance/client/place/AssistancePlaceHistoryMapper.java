package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;


@WithTokenizers({ PinAuthDataPlace.Tokenizer.class,
				  GeneralsJustificationPlace.Tokenizer.class,
				  JustificationPersonPlace.Tokenizer.class,
				  ManageJustificationPlace.Tokenizer.class,
				  PeriodsAssignationPersonPlace.Tokenizer.class,
				  DailyPeriodsPlace.Tokenizer.class })
public interface AssistancePlaceHistoryMapper extends PlaceHistoryMapper {

}
