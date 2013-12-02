package ar.com.dcsys.data.reserve;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;

public interface Reserve {
	
	public void setId(String id);
	public String getId();
	
	public Person getOwner();
	public void setOwner(Person owner);

	public Date getCreated();
	public void setCreated(Date created);

	public Reserve getRelated();
	public void setRelated(Reserve related);

	public List<Person> getRelatedPersons();
	public void setRelatedPersons(List<Person> relatedPersons);

	public ReserveAttemptDate getReserveAttemptDate();
	public void setReserveAttemptDate(ReserveAttemptDate reserveAttempt);

	public ClassRoom getClassRoom();
	public void setClassRoom(ClassRoom classRoom);

	public String getDescription();
	public void setDescription(String description);
	
	public Long getVersion();
	public void setVersion(Long version);
	
}
