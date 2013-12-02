package ar.com.dcsys.data.classroom;

import java.util.List;


public interface ClassRoom {
	
	public void setId(String id);
	public String getId();
	
	public String getName();
	public void setName(String name);
	
	public Boolean getDeleted();
	public void setDeleted(Boolean deleted);
	
	public List<CharacteristicQuantity> getCharacteristicQuantity();
	public void setCharacteristicQuantity(List<CharacteristicQuantity> characteristicQuantity);
	
	public Long getVersion();
	public void setVersion(Long version);
}
