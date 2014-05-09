package ar.com.dcsys.data.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;

public interface Period {
	
	public Person getPerson();
	public Date getStart();
	public Date getEnd();
	public List<? extends WorkedHours> getWorkedHours();
	
}
