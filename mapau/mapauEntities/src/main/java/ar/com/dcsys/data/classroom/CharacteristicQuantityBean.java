package ar.com.dcsys.data.classroom;

import java.io.Serializable;

public class CharacteristicQuantityBean implements Serializable, CharacteristicQuantity {

	private static final long serialVersionUID = 1L;
	
	private Characteristic characteristic;
	private Long quantity = 0l;

	public Characteristic getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(Characteristic characteristic) {
		this.characteristic = characteristic;
	}
	
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
}
