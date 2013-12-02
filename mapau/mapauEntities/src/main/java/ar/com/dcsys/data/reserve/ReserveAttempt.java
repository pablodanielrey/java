package ar.com.dcsys.data.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;

public interface ReserveAttempt {
		
	public void setId(String id);
	public String getId();
	
	public Person getOwner();
	public void setOwner(Person owner);

	public List<Person> getRelatedPersons();
	public void setRelatedPersons(List<Person> relatedPersons);

	public String getDescription();
	public void setDescription(String description);

	public Long getVersion();
	public void setVersion(Long version);

	public Date getCreated();
	public void setCreated(Date created);
	
}
