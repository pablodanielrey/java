package ar.com.dcsys.data.classroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClassRoomBean  implements Serializable, ClassRoom {
	
	private static final long serialVersionUID = 1L;

	private UUID id;
	public Long version = 1l;
	private String name;
	private Boolean deleted = false;
	
	private List<CharacteristicQuantity> characteristicQuantity = new ArrayList<CharacteristicQuantity>();

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	public String getId() {
		if (this.id == null) {
			return null;
		} else {
			return this.id.toString();
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public List<CharacteristicQuantity> getCharacteristicQuantity() {
		return this.characteristicQuantity;
	}
	
	public void setCharacteristicQuantity(List<CharacteristicQuantity> characteristicQuantity) {
		this.characteristicQuantity = characteristicQuantity;	
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
}
