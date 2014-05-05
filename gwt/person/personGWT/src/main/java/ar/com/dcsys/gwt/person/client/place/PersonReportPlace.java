package ar.com.dcsys.gwt.person.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PersonReportPlace extends Place {
    
    public PersonReportPlace(String token) { }
    
    public PersonReportPlace() { }
    
    public static class Tokenizer implements PlaceTokenizer<PersonReportPlace> {

            @Override
            public PersonReportPlace getPlace(String token) {
                    return new PersonReportPlace();
            }

            @Override
            public String getToken(PersonReportPlace place) {
                    return null;
            }
            
    }

}
