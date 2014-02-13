package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class MailChangePlace extends Place {
    
    public MailChangePlace(String token) { }
    
    public MailChangePlace() { }
    
    public static class Tokenizer implements PlaceTokenizer<MailChangePlace> {

            @Override
            public MailChangePlace getPlace(String token) {
                    return new MailChangePlace();
            }

            @Override
            public String getToken(MailChangePlace place) {
                    return null;
            }
            
    }

}
