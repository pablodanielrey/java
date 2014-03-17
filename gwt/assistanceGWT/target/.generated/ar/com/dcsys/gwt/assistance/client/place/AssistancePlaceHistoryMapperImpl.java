package ar.com.dcsys.gwt.assistance.client.place;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import ar.com.dcsys.gwt.assistance.client.place.AssistancePlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken;
import com.google.gwt.core.client.GWT;

public class AssistancePlaceHistoryMapperImpl extends AbstractPlaceHistoryMapper<Void> implements AssistancePlaceHistoryMapper {
  
  protected PrefixAndToken getPrefixAndToken(Place newPlace) {
    if (newPlace instanceof ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace) {
      ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace place = (ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace) newPlace;
      PlaceTokenizer<ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace> t = GWT.create(ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace.Tokenizer.class);
      return new PrefixAndToken("PinAuthDataPlace", t.getToken((ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace) place));
    }
    return null;
  }
  
  protected PlaceTokenizer<?> getTokenizer(String prefix) {
    if ("PinAuthDataPlace".equals(prefix)) {
      return GWT.create(ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace.Tokenizer.class);
    }
    return null;
  }

}
